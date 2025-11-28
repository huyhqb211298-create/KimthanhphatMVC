package com.example.kimthanhphatmvc.service.impl;

import com.example.kimthanhphatmvc.dto.NewsDTO;
import com.example.kimthanhphatmvc.model.News;
import com.example.kimthanhphatmvc.repository.NewsRepository;
import com.example.kimthanhphatmvc.service.CloudinaryService;
import com.example.kimthanhphatmvc.service.NewsService;
import com.example.kimthanhphatmvc.service.SlugService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final CloudinaryService cloudinaryService;
    private final SlugService slugService;

    @Override
    public List<News> findAll() {
        return newsRepository.findAll();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id).orElse(null);
    }

    @Override
    public News findBySlug(String slug) {
        return newsRepository.findBySlug(slug).orElse(null);
    }

    @Override
    public News save(NewsDTO dto) {
        News news = new News();

        news.setTitle(dto.getTitle());
        news.setSlug(slugService.createSlug(dto.getTitle()));
        news.setSummary(dto.getSummary());
        news.setContent(dto.getContent());
        news.setActive(dto.getActive() != null ? dto.getActive() : true);

        // Upload thumbnail
        if (dto.getThumbnailFile() != null && !dto.getThumbnailFile().isEmpty()) {
            String url = cloudinaryService.upload(dto.getThumbnailFile());
            news.setThumbnail(url);
        }

        return newsRepository.save(news);
    }
    @Override
    public List<News> findLatest(int limit) {
        return newsRepository.findAll(
                PageRequest.of(0, limit, Sort.by("createdAt").descending())
        ).getContent();
    }

    @Override
    public List<News> findRelated(Long excludeId, int limit) {
        return newsRepository.findRelated(excludeId, limit);
    }
    @Override
    public Page<News> search(String keyword, int page) {
        return newsRepository.search(
                keyword,
                PageRequest.of(page, 10, Sort.by("createdAt").descending())
        );
    }


    @Override
    public News update(Long id, NewsDTO dto) {
        News news = findById(id);
        if (news == null) return null;

        news.setTitle(dto.getTitle());
        news.setSlug(slugService.createSlug(dto.getTitle()));
        news.setSummary(dto.getSummary());
        news.setContent(dto.getContent());
        news.setActive(dto.getActive());

        // Có upload ảnh mới → replace
        MultipartFile newImg = dto.getThumbnailFile();
        if (newImg != null && !newImg.isEmpty()) {
            String url = cloudinaryService.upload(newImg);
            news.setThumbnail(url);
        }

        return newsRepository.save(news);
    }
    @Override
    public Page<News> findPage(int page) {
        return newsRepository.findAll(
                PageRequest.of(page, 10, Sort.by("createdAt").descending())
        );
    }
    @Override
    public void delete(Long id) {
        newsRepository.deleteById(id);
    }
}
