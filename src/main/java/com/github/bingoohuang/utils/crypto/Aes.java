package com.github.bingoohuang.utils.crypto;

import com.github.bingoohuang.utils.codec.Base64;
import com.github.bingoohuang.utils.codec.Bytes;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class Aes {
    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    public static String generateKey() {
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        kg.init(128);
        SecretKey secretKey = kg.generateKey();
        return Base64.base64(secretKey.getEncoded(), Base64.Format.Purified);
    }

    public static Key getKey(String key) {
        return new SecretKeySpec(Base64.unBase64(key), KEY_ALGORITHM);
    }

    public static String decrypt(String value, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.unBase64(value));
            return Bytes.string(decrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String encrypt(String value, Key key) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(Bytes.bytes(value));
            return Base64.base64(encrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
