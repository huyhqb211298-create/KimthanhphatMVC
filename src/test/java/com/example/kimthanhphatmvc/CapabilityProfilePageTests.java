package com.example.kimthanhphatmvc;

import com.example.kimthanhphatmvc.controller.HomeController;
import com.example.kimthanhphatmvc.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class CapabilityProfilePageTests {

    @Test
    void capabilityProfileProvidesEmbeddedViewerAndDirectPdfLinks() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new HomeController(mock(CategoryService.class)))
                .setViewResolvers(thymeleafViewResolver())
                .build();

        mockMvc.perform(get("/hsnl"))
                .andExpect(status().isOk())
                .andExpect(view().name("hsnl"))
                .andExpect(content().string(containsString("Hồ sơ năng lực Kim Thành Phát")))
                .andExpect(content().string(containsString("/files/hoso-nang-luc-kim-thanh-phat.pdf")))
                .andExpect(content().string(containsString("class=\"document-viewer\"")))
                .andExpect(content().string(containsString("/img/hsnl/hoso-nang-luc-6.jpg")))
                .andExpect(content().string(containsString("Tải PDF")));
    }

    private ThymeleafViewResolver thymeleafViewResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());

        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return viewResolver;
    }
}
