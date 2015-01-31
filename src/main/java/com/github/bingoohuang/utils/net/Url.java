package com.github.bingoohuang.utils.net;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Url {
    public static final String UTF_8 = "UTF-8";

    public static String encode(String s) {
        try {
            return URLEncoder.encode(s, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
