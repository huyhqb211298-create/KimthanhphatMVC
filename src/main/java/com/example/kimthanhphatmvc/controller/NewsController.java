package com.example.kimthanhphatmvc.controller;

import com.example.kimthanhphatmvc.model.News;
import com.example.kimthanhphatmvc.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    /**
     * Trang LIST chính: /news
     */
    @GetMapping
    public String listNews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        Page<News> newsPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            newsPage = newsService.search(keyword.trim(), page);
            model.addAttribute("keyword", keyword);
        } else {
            newsPage = newsService.findPage(page);
        }

        int totalPages = newsPage.getTotalPages();
        if (totalPages == 0) totalPages = 1;

        if (page >= totalPages) {
            page = totalPages - 1;
            newsPage = (keyword == null || keyword.isEmpty())
                    ? newsService.findPage(page)
                    : newsService.search(keyword, page);
        }

        News featured = newsPage.getContent().isEmpty() ? null : newsPage.getContent().get(0);

        var others = newsPage.getContent().size() > 1
                ? newsPage.getContent().subList(1, newsPage.getContent().size())
                : java.util.Collections.emptyList();

        model.addAttribute("featured", featured);
        model.addAttribute("others", others);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "public/news_list";
    }


    /**
     * API LOAD MORE: trả JSON các bài tiếp theo
     * /news/load?page=1,2,...
     */
    @GetMapping("/load")
    @ResponseBody
    public List<NewsDto> loadMore(@RequestParam int page) {
        if (page < 0) page = 0;

        Page<News> newsPage = newsService.findPage(page);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return newsPage.getContent().stream()
                .map(n -> new NewsDto(
                        n.getId(),
                        n.getTitle(),
                        n.getSlug(),
                        n.getThumbnail(),
                        n.getCreatedAt() != null ? n.getCreatedAt().format(fmt) : "",
                        buildSummary(n.getContent())
                ))
                .toList();
    }

    private String buildSummary(String content) {
        if (content == null) return "";
        String plain = content.replaceAll("<[^>]*>", ""); // bỏ tag HTML nếu có
        if (plain.length() <= 150) return plain;
        return plain.substring(0, 150) + "...";
    }

    @GetMapping("/{slug}")
    public String detail(@PathVariable String slug, Model model) {

        News news = newsService.findBySlug(slug);
        if (news == null) return "redirect:/news";

        // Tin mới nhất (5 bài)
        var latest = newsService.findLatest(5);

        // Tin liên quan: cùng category hoặc random (hiện tạm random 6 bài)
        var related = newsService.findRelated(news.getId(), 6);

        model.addAttribute("news", news);
        model.addAttribute("latest", latest);
        model.addAttribute("related", related);

        return "public/news_detail";
    }



    // DTO dùng cho JSON
    public record NewsDto(
            Long id,
            String title,
            String slug,
            String thumbnail,
            String createdAt,
            String summary
    ) {}
}
