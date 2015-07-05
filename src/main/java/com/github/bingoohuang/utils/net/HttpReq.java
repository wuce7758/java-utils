package com.github.bingoohuang.utils.net;

import com.github.bingoohuang.utils.codec.Json;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class HttpReq {
    private final String baseUrl;
    private String req;
    private StringBuilder params = new StringBuilder();
    Logger logger = LoggerFactory.getLogger(HttpReq.class);
    private List<Pair<String, String>> props = Lists.newArrayList();
    private String body;
    private String anchor;

    public HttpReq(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static HttpReq get(String baseUrl) {
        return new HttpReq(baseUrl);
    }

    public HttpReq req(String req) {
        this.req = req;
        return this;
    }

    public HttpReq body(String body) {
        this.body = body;
        return this;
    }

    public HttpReq anchor(String anchor) {
        this.anchor = anchor;
        return this;
    }

    public HttpReq cookie(String value) {
        if (value == null) return this;

        return prop("Cookie", value);
    }

    public HttpReq prop(String name, String value) {
        props.add(Pair.of(name, value));
        return this;
    }

    public HttpReq param(String name, String value) {
        if (params.length() > 0) params.append('&');
        params.append(name).append('=').append(Url.encode(value));

        return this;
    }

    public HttpReq requestBody(String requestBody) {
        if (requestBody != null) {
            if (params.length() > 0) params.append('&');
            params.append(requestBody);
        }

        return this;
    }

    public String post() {
        HttpURLConnection http = null;
        try {
            String url = baseUrl + (req == null ? "" : req)
                    + (params.length() > 0 && body != null? ("?" + params) : "")
                    + (anchor == null ? "" : "#" + anchor);

            http = commonSettings(url);
            setHeaders(http);
            postSettings(http);

            // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
            // 要注意的是connection.getOutputStream会隐含的进行connect。
            http.connect();

            writePostRequestBody(http);

            return parseResponse(http, url);
        } catch (Exception e) {
            logger.error("post error {}", e.getMessage());
            return null;
        } finally {
            if (http != null) http.disconnect();
        }
    }

    private void postSettings(HttpURLConnection http) throws ProtocolException {
        // 设置是否向connection输出，因为这个是post请求，参数要放在
        // http正文内，因此需要设为true
        http.setDoOutput(true);
        http.setDoInput(true); // Read from the connection. Default is true.
        http.setRequestMethod("POST");// 默认是 GET方式
        http.setUseCaches(false); // Post 请求不能使用缓存
        http.setInstanceFollowRedirects(true);
        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
        // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode进行编码
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    }


    public String get() {
        HttpURLConnection http = null;
        try {
            String url = baseUrl + (req == null ? "" : req)
                    + (params.length() > 0 ? ("?" + params) : "")
                    + (anchor == null ? "" : "#" + anchor);

            http = commonSettings(url);
            setHeaders(http);
            http.connect();

            return parseResponse(http, url);
        } catch (Exception e) {
            logger.error("get error {}", e.getMessage());
            return null;
        } finally {
            if (http != null) http.disconnect();
        }
    }

    private void setHeaders(HttpURLConnection http) {
        for (Pair<String, String> prop : props)
            http.setRequestProperty(prop.getKey(), prop.getValue());
    }

    private HttpURLConnection commonSettings(String url) throws IOException {
        HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
        http.setRequestProperty("Accept-Charset", "UTF-8");
        HttpURLConnection.setFollowRedirects(true);
        http.setConnectTimeout(60 * 1000);
        http.setReadTimeout(60 * 1000);
        return http;
    }

    private void writePostRequestBody(HttpURLConnection http) throws IOException {
        if (params.length() == 0 && StringUtils.isEmpty(body)) return;

        if (StringUtils.isNotEmpty(body)) {
            OutputStreamWriter out = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            out.append(body);
            out.flush();
            out.close();
        } else {
            DataOutputStream out = new DataOutputStream(http.getOutputStream());
            // The URL-encoded contend 正文，正文内容其实跟get的URL中 '? '后的参数字符串一致
            // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写到流里面
            String postData = params.toString();
            out.writeBytes(postData);
            out.flush();
            out.close();
        }
    }

    private String parseResponse(HttpURLConnection http, String url) throws IOException {
        int status = http.getResponseCode();
        String charset = getCharset(http.getHeaderField("Content-Type"));

        if (status == 200) return readResponseBody(http, charset);

        logger.warn("non 200 response :" + readErrorResponseBody(url, http, status, charset));
        return null;
    }

    private String readErrorResponseBody(String url, HttpURLConnection http, int status, String charset) throws IOException {
        InputStream errorStream = http.getErrorStream();
        if (errorStream != null) {
            String error = toString(charset, errorStream);
            return (url + ", STATUS CODE =" + status + ", headers=" + Json.json(http.getHeaderFields()) + "\n\n" + error);
        } else {
            return (url + ", STATUS CODE =" + status + ", headers=" + Json.json(http.getHeaderFields()));
        }
    }

    private static String readResponseBody(HttpURLConnection http, String charset) throws IOException {
        InputStream inputStream = http.getInputStream();

        return toString(charset, inputStream);
    }

    private static String toString(String charset, InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }

        return new String(baos.toByteArray(), charset);
    }

    private static String getCharset(String contentType) {
        if (contentType == null) return "UTF-8";

        String charset = null;
        for (String param : contentType.replace(" ", "").split(";")) {
            if (param.startsWith("charset=")) {
                charset = param.split("=", 2)[1];
                break;
            }
        }

        return charset == null ? "UTF-8" : charset;
    }
}
