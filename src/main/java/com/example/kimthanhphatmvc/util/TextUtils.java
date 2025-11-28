package com.example.kimthanhphatmvc.util;

import java.text.Normalizer;

public class TextUtils {

    public static String removeAccent(String text) {
        if (text == null) return null;

        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        // bỏ toàn bộ dấu
        String noAccent = normalized.replaceAll("\\p{M}", "");
        // xử lý riêng đ/Đ
        noAccent = noAccent.replace("đ", "d").replace("Đ", "D");
        return noAccent;
    }
}
