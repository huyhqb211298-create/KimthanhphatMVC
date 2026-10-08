package com.example.kimthanhphatmvc;

import com.example.kimthanhphatmvc.controller.HomeController;
import com.example.kimthanhphatmvc.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class HomePageTests {

    @Test
    void rendersRedesignedHomepageWithFeaturedProductSystems() throws Exception {
        CategoryService categoryService = mock(CategoryService.class);
        when(categoryService.findAll()).thenReturn(Collections.emptyList());

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

        MockMvc mvc = MockMvcBuilders
                .standaloneSetup(new HomeController(categoryService))
                .setViewResolvers(views)
                .build();

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("Xem toàn bộ sản phẩm")))
                .andExpect(content().string(containsString("Sản phẩm cho")))
                .andExpect(content().string(containsString("Thiết bị chữa cháy ban đầu")))
                .andExpect(content().string(containsString("Xem các sản phẩm")))
                .andExpect(content().string(containsString("/img/fire-extinguisher-lineup-studio.png")))
                .andExpect(content().string(containsString("Phát hiện &amp; cảnh báo GST")))
                .andExpect(content().string(containsString("Xem sản phẩm GST")))
                .andExpect(content().string(containsString("Bình chữa cháy DeFire")))
                .andExpect(content().string(containsString("Khí sạch FM-200")))
                .andExpect(content().string(containsString("/img/defire-df-abc4-studio.png")))
                .andExpect(content().string(containsString("Thương hiệu đồng hành")))
                .andExpect(content().string(containsString("/img/partners/gst.png")))
                .andExpect(content().string(containsString("/img/partners/protector.png")))
                .andExpect(content().string(containsString("/img/partners/mintai.svg")));
    }
}
