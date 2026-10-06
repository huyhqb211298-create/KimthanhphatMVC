package com.example.kimthanhphatmvc;

import com.example.kimthanhphatmvc.controller.NewsController;
import com.example.kimthanhphatmvc.model.News;
import com.example.kimthanhphatmvc.service.NewsService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class NewsPageTests {

    @Test
    void rendersNewsListInTheSiteConcept() throws Exception {
        NewsService service = mock(NewsService.class);
        List<News> items = List.of(news(1L, "Tin nổi bật", "tin-noi-bat"), news(2L, "Hướng dẫn an toàn", "huong-dan-an-toan"));
        when(service.findPage(0)).thenReturn(new PageImpl<>(items, PageRequest.of(0, 10), items.size()));

        mockMvc(service).perform(get("/news"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/news_list"))
                .andExpect(content().string(containsString("An toàn bắt đầu từ thông tin đúng")))
                .andExpect(content().string(containsString("/css/news.css")))
                .andExpect(content().string(containsString("href=\"/news/tin-noi-bat\"")))
                .andExpect(content().string(containsString("href=\"/contact\"")));
    }

    @Test
    void rendersNewsDetailWithLatestAndRelatedArticles() throws Exception {
        NewsService service = mock(NewsService.class);
        News article = news(1L, "Kiểm tra bình chữa cháy", "kiem-tra-binh-chua-chay");
        News related = news(2L, "Hướng dẫn an toàn", "huong-dan-an-toan");
        when(service.findBySlug(article.getSlug())).thenReturn(article);
        when(service.findLatest(5)).thenReturn(List.of(article, related));
        when(service.findRelated(article.getId(), 6)).thenReturn(List.of(related));

        mockMvc(service).perform(get("/news/" + article.getSlug()))
                .andExpect(status().isOk())
                .andExpect(view().name("public/news_detail"))
                .andExpect(content().string(containsString("Kiểm tra bình chữa cháy")))
                .andExpect(content().string(containsString("Bài viết liên quan")))
                .andExpect(content().string(containsString("class=\"news-card-media\"")))
                .andExpect(content().string(containsString("/css/news.css")));
    }

    private MockMvc mockMvc(NewsService service) {
        ClassLoaderTemplateResolver templates = new ClassLoaderTemplateResolver();
        templates.setPrefix("templates/");
        templates.setSuffix(".html");
        templates.setTemplateMode("HTML");
        templates.setCharacterEncoding("UTF-8");
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templates);
        ThymeleafViewResolver views = new ThymeleafViewResolver();
        views.setTemplateEngine(engine);
        views.setCharacterEncoding("UTF-8");

        return MockMvcBuilders.standaloneSetup(new NewsController(service))
                .setViewResolvers(views)
                .build();
    }

    private News news(Long id, String title, String slug) {
        return News.builder()
                .id(id)
                .title(title)
                .slug(slug)
                .thumbnail("/img/about-banner.jpg")
                .summary("Thông tin ngắn gọn giúp chủ động bảo đảm an toàn PCCC.")
                .content("<p>Nội dung hướng dẫn an toàn cho công trình.</p>")
                .createdAt(LocalDateTime.of(2026, 10, 5, 9, 0))
                .active(true)
                .published(true)
                .build();
    }
}
