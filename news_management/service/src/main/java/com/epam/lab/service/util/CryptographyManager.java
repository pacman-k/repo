package com.epam.lab.service.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CryptographyManager {

    public static String encode (String text){
        return text == null ? null : Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String decode(String text){
        return text == null ? null : new String(Base64.getDecoder().decode(text.getBytes()), StandardCharsets.UTF_8);
    }

}
