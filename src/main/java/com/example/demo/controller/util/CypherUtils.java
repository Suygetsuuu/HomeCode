package com.example.demo.controller.util;

import java.util.Base64;

public class CypherUtils {

    public static String base64Decryption(String encodedInfo) {
        // 对Base64编码进行解码
        byte[] decodedBytes = Base64.getDecoder().decode(encodedInfo);
        String decodedInfo = new String(decodedBytes);
        return decodedInfo;
    }

    public static String base64Encryption(String originInfo) {
        return Base64.getEncoder().encodeToString(originInfo.getBytes());
    }
}
