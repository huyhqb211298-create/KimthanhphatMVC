package com.example.kimthanhphatmvc;

import com.example.kimthanhphatmvc.controller.SitemapController;
import com.example.kimthanhphatmvc.model.Brand;
import com.example.kimthanhphatmvc.model.Category;
import com.example.kimthanhphatmvc.model.News;
import com.example.kimthanhphatmvc.model.Product;
import com.example.kimthanhphatmvc.model.ProductType;
import com.example.kimthanhphatmvc.service.BrandService;
import com.example.kimthanhphatmvc.service.CategoryService;
import com.example.kimthanhphatmvc.service.NewsService;
import com.example.kimthanhphatmvc.service.ProductService;
import com.example.kimthanhphatmvc.service.ProductTypeService;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SitemapControllerTests {

    @Test
    void sitemapIncludesPublicCatalogAndPublishedNewsOnly() {
        ProductService productService = mock(ProductService.class);
        NewsService newsService = mock(NewsService.class);
        CategoryService categoryService = mock(CategoryService.class);
        BrandService brandService = mock(BrandService.class);
        ProductTypeService productTypeService = mock(ProductTypeService.class);

        Product product = new Product();
        product.setSlug("binh-chua-chay-defire");
        Category category = new Category();
        category.setSlug("thiet-bi-chua-chay");
        Brand brand = new Brand();
        brand.setSlug("gst");
        ProductType type = new ProductType();
        type.setSlug("binh-chua-chay");
        News published = News.builder()
                .slug("huong-dan-pccc")
                .active(true)
                .published(true)
                .updatedAt(LocalDateTime.of(2026, 10, 12, 9, 0))
                .build();
        News draft = News.builder()
                .slug("tin-chua-xuat-ban")
                .active(true)
                .published(false)
                .build();

        when(productService.findAll()).thenReturn(List.of(product));
        when(categoryService.findAll()).thenReturn(List.of(category));
        when(brandService.findAll()).thenReturn(List.of(brand));
        when(productTypeService.findAll()).thenReturn(List.of(type));
        when(newsService.findAll()).thenReturn(List.of(published, draft));

        SitemapController controller = new SitemapController(
                productService, newsService, categoryService, brandService, productTypeService,
                "https://kimthanhphatpccc.vn/");

        String xml = controller.sitemap();

        assertThat(xml)
                .contains("<loc>https://kimthanhphatpccc.vn/</loc>")
                .contains("<loc>https://kimthanhphatpccc.vn/products/binh-chua-chay-defire</loc>")
                .contains("<loc>https://kimthanhphatpccc.vn/thiet-bi-chua-chay</loc>")
                .contains("<loc>https://kimthanhphatpccc.vn/gst</loc>")
                .contains("<loc>https://kimthanhphatpccc.vn/binh-chua-chay</loc>")
                .contains("<loc>https://kimthanhphatpccc.vn/news/huong-dan-pccc</loc>")
                .contains("<lastmod>2026-10-12</lastmod>")
                .doesNotContain("tin-chua-xuat-ban");
    }
}
