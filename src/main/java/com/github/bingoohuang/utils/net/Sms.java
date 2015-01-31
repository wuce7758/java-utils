package com.github.bingoohuang.utils.net;

import com.github.bingoohuang.utils.net.HttpReq;

public class Sms {
    /**
     * 发短信
     *
     * @param apiKey 云片APIKEY
     * @param text   短信内容
     * @param mobile 接受的手机号
     * @return json格式字符串
     */
    public static String sendSms(String apiKey,  String text, String mobile) {
        return new HttpReq("http://yunpian.com/v1/sms/send.json")
                .param("apikey", apiKey)
                .param("text", text)
                .param("mobile", mobile)
                .post();
    }
}