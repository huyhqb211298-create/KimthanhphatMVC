package com.example.kimthanhphatmvc.service;

import com.example.kimthanhphatmvc.model.News;
import java.util.List;
import java.util.Optional;

public interface NewsService {
    List<News> findAll();
    Optional<News> findById(Long id);
    News save(News news);
    void deleteById(Long id);
}
