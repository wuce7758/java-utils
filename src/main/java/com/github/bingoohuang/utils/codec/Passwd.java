package com.github.bingoohuang.utils.codec;

public class Passwd {
    public static String bcrypt(String originalPassword) {
        return BCrypt.hashpw(originalPassword, BCrypt.gensalt(12));
    }

    public static boolean bcryptMatch(String originalPassword, String securedPasswordHash) {
        return BCrypt.checkpw(originalPassword, securedPasswordHash);
    }
}
