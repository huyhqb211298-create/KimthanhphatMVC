package com.example.kimthanhphatmvc.service.impl;

import com.example.kimthanhphatmvc.model.News;
import com.example.kimthanhphatmvc.repository.NewsRepository;
import com.example.kimthanhphatmvc.service.NewsService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public List<News> findAll() {
        return newsRepository.findAll();
    }

    @Override
    public Optional<News> findById(Long id) {
        return newsRepository.findById(id);
    }

    @Override
    public News save(News news) {
        return newsRepository.save(news);
    }

    @Override
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }
}
