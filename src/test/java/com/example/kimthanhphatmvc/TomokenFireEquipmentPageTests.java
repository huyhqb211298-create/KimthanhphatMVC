package com.example.kimthanhphatmvc;

import com.example.kimthanhphatmvc.controller.HomeController;
import com.example.kimthanhphatmvc.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class TomokenFireEquipmentPageTests {

    @Test
    void rendersTomokenPageWithProductGroupsAndContactCta() throws Exception {
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
                .standaloneSetup(new HomeController(mock(CategoryService.class)))
                .setViewResolvers(views)
                .build();

        mvc.perform(get("/thiet-bi-chua-chay-tomoken"))
                .andExpect(status().isOk())
                .andExpect(view().name("tomoken-fire-equipment"))
                .andExpect(content().string(containsString("id=\"binh-chua-chay\"")))
                .andExpect(content().string(containsString("id=\"cap-nuoc\"")))
                .andExpect(content().string(containsString("id=\"tu-phu-kien\"")))
                .andExpect(content().string(containsString("href=\"/thiet-bi-chua-chay-tomoken\"")))
                .andExpect(content().string(containsString("href=\"/contact\"")));
    }
}
