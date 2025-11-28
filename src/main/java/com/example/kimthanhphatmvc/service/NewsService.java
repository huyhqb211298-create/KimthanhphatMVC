package com.example.kimthanhphatmvc.service;

import com.example.kimthanhphatmvc.dto.NewsDTO;
import com.example.kimthanhphatmvc.model.News;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NewsService {

    List<News> findAll();

    News findById(Long id);

    News findBySlug(String slug);

    News save(NewsDTO dto);

    News update(Long id, NewsDTO dto);
    Page<News> findPage(int page);

    void delete(Long id);
    List<News> findLatest(int limit);
    List<News> findRelated(Long excludeId, int limit);
    Page<News> search(String keyword, int page);

}
