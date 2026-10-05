package com.example.kimthanhphatmvc;

import com.example.kimthanhphatmvc.controller.SlugController;
import com.example.kimthanhphatmvc.model.Brand;
import com.example.kimthanhphatmvc.model.Category;
import com.example.kimthanhphatmvc.model.ProductType;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SlugCatalogPageTests {

    @Test
    void categoryPageUsesSharedCatalogDesign() throws Exception {
        Fixture f = fixture();
        when(f.categories.findBySlug("thiet-bi-bao-chay")).thenReturn(Optional.of(f.category));
        when(f.products.findByCategoryPaged(anyLong(), anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of()));

        f.mvc.perform(get("/thiet-bi-bao-chay"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/category"))
                .andExpect(content().string(containsString("class=\"products-page\"")))
                .andExpect(content().string(containsString("/css/product-pages.css")))
                .andExpect(content().string(containsString("Sản phẩm trong nhóm này")));
    }

    @Test
    void brandAndProductTypePagesUseSharedCatalogDesign() throws Exception {
        Fixture brandFixture = fixture();
        when(brandFixture.brands.findBySlug("gst")).thenReturn(Optional.of(brandFixture.brand));
        when(brandFixture.products.findByBrandPaged(anyLong(), anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of()));
        brandFixture.mvc.perform(get("/gst"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/brand"))
                .andExpect(content().string(containsString("catalog-hero-compact")));

        Fixture typeFixture = fixture();
        when(typeFixture.types.findBySlug("dau-bao-chay")).thenReturn(Optional.of(typeFixture.type));
        when(typeFixture.products.findByProductTypePaged(anyLong(), anyInt(), anyInt())).thenReturn(new PageImpl<>(List.of()));
        typeFixture.mvc.perform(get("/dau-bao-chay"))
                .andExpect(status().isOk())
                .andExpect(view().name("public/product_type"))
                .andExpect(content().string(containsString("catalog-hero-compact")));
    }

    private Fixture fixture() {
        ProductService products = mock(ProductService.class);
        CategoryService categories = mock(CategoryService.class);
        BrandService brands = mock(BrandService.class);
        ProductTypeService types = mock(ProductTypeService.class);

        Category category = new Category();
        category.setId(1L);
        category.setName("Thiết bị báo cháy");
        category.setSlug("thiet-bi-bao-chay");
        Brand brand = new Brand();
        brand.setId(2L);
        brand.setName("GST");
        brand.setSlug("gst");
        ProductType type = new ProductType();
        type.setId(3L);
        type.setName("Đầu báo cháy");
        type.setSlug("dau-bao-chay");

        when(products.findBySlug(org.mockito.ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        when(categories.findBySlug(org.mockito.ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        when(brands.findBySlug(org.mockito.ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        when(types.findBySlug(org.mockito.ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        when(categories.findAll()).thenReturn(List.of(category));
        when(brands.findAll()).thenReturn(List.of(brand));
        when(types.findAll()).thenReturn(List.of(type));

        SlugController controller = new SlugController(products, categories, brands, types);
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

        return new Fixture(mvc, products, categories, brands, types, category, brand, type);
    }

    private record Fixture(MockMvc mvc, ProductService products, CategoryService categories,
                           BrandService brands, ProductTypeService types, Category category,
                           Brand brand, ProductType type) { }
}
