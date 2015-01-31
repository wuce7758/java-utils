package com.github.bingoohuang.utils.codec;

import com.google.common.base.Charsets;

public class Bytes {
    public static byte[] bytes(String str) {
        return str == null ? null : str.getBytes(Charsets.UTF_8);
    }

    public static String string(byte[] bytes) {
        return new String(bytes, Charsets.UTF_8);
    }
}
