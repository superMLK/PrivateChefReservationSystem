package com.example.privatechefreservationsystem.utils;

public class AuthUtil {
    /** 解密密碼 */
    public static String decryptPassword(String encryptedPassword) {
        if (encryptedPassword == null || encryptedPassword.isEmpty()) {
            throw new IllegalArgumentException("加密密碼不能為空");
        }

        try {
            // Base64 解碼
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(encryptedPassword);
            return new String(decodedBytes, java.nio.charset.StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("密碼格式錯誤：不是有效的 Base64 字串", e);
        }
    }
}
