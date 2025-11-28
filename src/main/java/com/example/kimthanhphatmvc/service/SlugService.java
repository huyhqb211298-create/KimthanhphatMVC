package com.example.kimthanhphatmvc.service;

public interface SlugService {

    boolean existsInAnyTable(String slug);

    String generateUniqueSlug(String baseSlug);

    String createSlug(String name);
}
