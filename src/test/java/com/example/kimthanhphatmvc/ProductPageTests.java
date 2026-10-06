package com.example.kimthanhphatmvc;

import com.example.kimthanhphatmvc.controller.ProductController;
import com.example.kimthanhphatmvc.model.Brand;
import com.example.kimthanhphatmvc.model.Category;
import com.example.kimthanhphatmvc.model.Product;
import com.example.kimthanhphatmvc.model.ProductType;
import com.example.kimthanhphatmvc.model.enums.PriceDisplay;
import com.example.kimthanhphatmvc.model.enums.StockStatus;
import com.example.kimthanhphatmvc.service.BrandService;
import com.example.kimthanhphatmvc.service.CategoryService;
import com.example.kimthanhphatmvc.service.ProductService;
import com.example.kimthanhphatmvc.service.ProductTypeService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductPageTests {

    @Test
    void productListUsesNewCatalogDesignAndCanonicalDetailLinks() throws Exception {
        Fixture fixture = fixture();
        when(fixture.products.findFiltered(any(), any(), any(), any(), eq(1), eq(9)))
                .thenReturn(new PageImpl<>(List.of(fixture.product)));

        fixture.mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/product_list"))
                .andExpect(content().string(containsString("class=\"products-page\"")))
                .andExpect(content().string(containsString("/css/product-pages.css")))
                .andExpect(content().string(containsString("Thiết bị an toàn cho mọi công trình")))
                .andExpect(content().string(containsString("fire-extinguisher-lineup-studio.png")))
                .andExpect(content().string(containsString("href=\"/products/binh-chua-chay-mau\"")));
    }

    @Test
    void productDetailUsesNewShowcaseAndConsultationActions() throws Exception {
        Fixture fixture = fixture();
        fixture.product.setPrice(null);
        when(fixture.products.findBySlug("binh-chua-chay-mau")).thenReturn(Optional.of(fixture.product));
        when(fixture.products.findRelatedProducts(fixture.product)).thenReturn(List.of());

        fixture.mvc.perform(get("/products/binh-chua-chay-mau"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/product_detail"))
                .andExpect(content().string(containsString("class=\"products-page\"")))
                .andExpect(content().string(containsString("class=\"product-showcase\"")))
                .andExpect(content().string(containsString("Nhận tư vấn qua Zalo")))
                .andExpect(content().string(containsString("Liên hệ để nhận báo giá theo nhu cầu")))
                .andExpect(content().string(not(containsString("null ₫"))))
                .andExpect(content().string(containsString("https://kimthanhphatpccc.vn/products/binh-chua-chay-mau")));
    }

    private Fixture fixture() {
        ProductService products = mock(ProductService.class);
        CategoryService categories = mock(CategoryService.class);
        BrandService brands = mock(BrandService.class);
        ProductTypeService types = mock(ProductTypeService.class);

        Category category = new Category();
        category.setId(1L);
        category.setName("Bình chữa cháy");
        category.setSlug("binh-chua-chay");

        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("DEFire");
        brand.setSlug("defire");

        ProductType type = new ProductType();
        type.setId(1L);
        type.setName("Bình bột");
        type.setSlug("binh-bot");

        Product product = new Product();
        product.setId(1L);
        product.setName("Bình chữa cháy mẫu");
        product.setSlug("binh-chua-chay-mau");
        product.setImage("/img/no-image.jpg");
        product.setPrice(350000D);
        product.setPriceDisplay(PriceDisplay.SHOW);
        product.setStockStatus(StockStatus.IN_STOCK);
        product.setOrigin("Việt Nam");
        product.setWarrantyMonths(12);
        product.setDescription("Sản phẩm dùng cho kiểm thử giao diện.");
        product.setCategory(category);
        product.setBrand(brand);
        product.setProductType(type);

        when(categories.findAll()).thenReturn(List.of(category));
        when(brands.findAll()).thenReturn(List.of(brand));
        when(types.findAll()).thenReturn(List.of(type));

        ProductController controller = new ProductController(products, categories, brands, types);
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

        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(views).build();
        return new Fixture(mvc, products, product);
    }

    private record Fixture(MockMvc mvc, ProductService products, Product product) { }
}
