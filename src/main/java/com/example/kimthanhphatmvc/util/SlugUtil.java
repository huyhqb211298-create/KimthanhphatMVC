package com.example.kimthanhphatmvc.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtil {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String toSlug(String input) {
        if (input == null || input.isEmpty()) return "";

        // Chuẩn hóa tiếng Việt
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replace("Đ", "D")
                .replace("đ", "d");

        // Thay khoảng trắng bằng dấu gạch ngang
        String noWhitespace = WHITESPACE.matcher(normalized).replaceAll("-");
        String slug = NONLATIN.matcher(noWhitespace).replaceAll("");
        slug = slug.replaceAll("-{2,}", "-"); // tránh trường hợp nhiều dấu '-'
        slug = slug.replaceAll("^-|-$", ""); // xóa dấu '-' đầu/cuối
        return slug.toLowerCase(Locale.ROOT);
    }

    /**
     * Sinh slug có hậu tố nếu bị trùng
     * @param baseSlug slug gốc
     * @param count số lần trùng
     */
    public static String uniqueSlug(String baseSlug, int count) {
        return count <= 0 ? baseSlug : baseSlug + "-" + count;
    }
}
