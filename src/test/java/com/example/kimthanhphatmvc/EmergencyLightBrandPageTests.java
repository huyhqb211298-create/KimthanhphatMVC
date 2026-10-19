package com.example.kimthanhphatmvc;

import com.example.kimthanhphatmvc.controller.HomeController;
import com.example.kimthanhphatmvc.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class EmergencyLightBrandPageTests {

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
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

        mvc = MockMvcBuilders.standaloneSetup(new HomeController(mock(CategoryService.class)))
                .setViewResolvers(views)
                .build();
    }

    @Test
    void rendersKentomCatalogWithoutPriceColumns() throws Exception {
        mvc.perform(get("/den-su-co-exit-kentom"))
                .andExpect(status().isOk())
                .andExpect(view().name("kentom-emergency-light"))
                .andExpect(content().string(containsString("KT2200EL")))
                .andExpect(content().string(containsString("KT670")))
                .andExpect(content().string(containsString("kentom-studio-kt2200el.webp")))
                .andExpect(content().string(containsString("Ảnh sản phẩm từ catalogue Kentom")))
                .andExpect(content().string(not(containsString("279.720"))))
                .andExpect(content().string(not(containsString("1.013.040"))));
    }

    @Test
    void rendersLilangCatalogWithoutPriceColumns() throws Exception {
        mvc.perform(get("/den-su-co-exit-lilang"))
                .andExpect(status().isOk())
                .andExpect(view().name("lilang-emergency-light"))
                .andExpect(content().string(containsString("XF-ZFZD-E3WA")))
                .andExpect(content().string(containsString("XF-BLZD-2LREI 3WA")))
                .andExpect(content().string(containsString("lilang-studio-e3wa.webp")))
                .andExpect(content().string(not(containsString("145.800"))))
                .andExpect(content().string(not(containsString("124.200"))));
    }
}
