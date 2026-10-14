package com.example.kimthanhphatmvc.controller;

import com.example.kimthanhphatmvc.model.News;
import com.example.kimthanhphatmvc.service.BrandService;
import com.example.kimthanhphatmvc.service.CategoryService;
import com.example.kimthanhphatmvc.service.NewsService;
import com.example.kimthanhphatmvc.service.ProductService;
import com.example.kimthanhphatmvc.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SitemapController {

    private static final List<String> STATIC_PATHS = List.of(
            "/", "/about", "/contact", "/products", "/news", "/documents", "/faq", "/hsnl",
            "/he-thong-chua-chay-khi-fm-200-stec-vina", "/he-thong-bao-chay-gst", "/thiet-bi-chua-chay-tomoken",
            "/documents/quy-dinh-thiet-ke-pccc", "/documents/thu-tuc-phe-duyet-phuong-an-pccc",
            "/documents/ho-so-tham-duyet-pccc", "/documents/ho-so-nghiem-thu-pccc",
            "/documents/thong-tu-47", "/documents/thong-tu-66-2014",
            "/documents/qcvn-08-2009", "/documents/qcvn-12-2014"
    );

    private final ProductService productService;
    private final NewsService newsService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductTypeService productTypeService;
    private final String siteUrl;

    public SitemapController(ProductService productService, NewsService newsService,
                             CategoryService categoryService, BrandService brandService,
                             ProductTypeService productTypeService,
                             @Value("${app.site-url:https://kimthanhphatpccc.vn}") String siteUrl) {
        this.productService = productService;
        this.newsService = newsService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.productTypeService = productTypeService;
        this.siteUrl = siteUrl.replaceAll("/+$", "");
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemap() {
        Map<String, LocalDate> urls = new LinkedHashMap<>();
        STATIC_PATHS.forEach(path -> urls.put(siteUrl + path, null));

        productService.findAll().stream()
                .filter(product -> hasSlug(product.getSlug()))
                .forEach(product -> urls.put(siteUrl + "/products/" + product.getSlug(), null));
        categoryService.findAll().stream()
                .filter(category -> hasSlug(category.getSlug()))
                .forEach(category -> urls.put(siteUrl + "/" + category.getSlug(), null));
        brandService.findAll().stream()
                .filter(brand -> hasSlug(brand.getSlug()))
                .forEach(brand -> urls.put(siteUrl + "/" + brand.getSlug(), null));
        productTypeService.findAll().stream()
                .filter(type -> hasSlug(type.getSlug()))
                .forEach(type -> urls.put(siteUrl + "/" + type.getSlug(), null));
        newsService.findAll().stream()
                .filter(this::isPublished)
                .filter(news -> hasSlug(news.getSlug()))
                .forEach(news -> urls.put(siteUrl + "/news/" + news.getSlug(), lastModified(news)));

        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");
        urls.forEach((url, lastModified) -> {
            xml.append("  <url><loc>").append(HtmlUtils.htmlEscape(url)).append("</loc>");
            if (lastModified != null) {
                xml.append("<lastmod>").append(lastModified).append("</lastmod>");
            }
            xml.append("</url>\n");
        });
        return xml.append("</urlset>").toString();
    }

    private boolean isPublished(News news) {
        return Boolean.TRUE.equals(news.getActive()) && Boolean.TRUE.equals(news.getPublished());
    }

    private LocalDate lastModified(News news) {
        if (news.getUpdatedAt() != null) {
            return news.getUpdatedAt().toLocalDate();
        }
        return news.getCreatedAt() == null ? null : news.getCreatedAt().toLocalDate();
    }

    private boolean hasSlug(String slug) {
        return slug != null && slug.matches("[a-z0-9-]+");
    }
}
