package com.example.kimthanhphatmvc;

import com.example.kimthanhphatmvc.controller.DocumentController;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class DocumentPageTests {

    private static final List<String> SLUGS = List.of(
            "quy-dinh-thiet-ke-pccc",
            "thu-tuc-phe-duyet-phuong-an-pccc",
            "ho-so-tham-duyet-pccc",
            "ho-so-nghiem-thu-pccc",
            "thong-tu-47",
            "thong-tu-66-2014",
            "qcvn-08-2009",
            "qcvn-12-2014"
    );

    @Test
    void rendersDocumentIndexWithEveryPublicDocument() throws Exception {
        MockMvc mvc = mockMvc();

        mvc.perform(get("/documents"))
                .andExpect(status().isOk())
                .andExpect(view().name("documents"))
                .andExpect(content().string(containsString("Văn bản rõ ràng cho công trình an toàn")))
                .andExpect(content().string(containsString("/css/documents.css")));

        for (String slug : SLUGS) {
            mvc.perform(get("/documents"))
                    .andExpect(content().string(containsString("href=\"/documents/" + slug + "\"")));
        }
    }

    @Test
    void rendersEveryDocumentDetailWithSharedDesign() throws Exception {
        MockMvc mvc = mockMvc();

        for (String slug : SLUGS) {
            mvc.perform(get("/documents/" + slug))
                    .andExpect(status().isOk())
                    .andExpect(view().name("documents/" + slug))
                    .andExpect(content().string(containsString("class=\"documents-page\"")))
                    .andExpect(content().string(containsString("/css/documents.css")))
                    .andExpect(content().string(containsString("Quay lại danh sách văn bản")));
        }
    }

    @Test
    void unknownDocumentReturnsNotFound() throws Exception {
        mockMvc().perform(get("/documents/khong-ton-tai"))
                .andExpect(status().isNotFound());
    }

    private MockMvc mockMvc() {
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

        return MockMvcBuilders.standaloneSetup(new DocumentController())
                .setViewResolvers(views)
                .build();
    }
}
