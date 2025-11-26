package com.example.kimthanhphatmvc.controller;

import com.example.kimthanhphatmvc.model.Product;
import com.example.kimthanhphatmvc.service.BrandService;
import com.example.kimthanhphatmvc.service.CategoryService;
import com.example.kimthanhphatmvc.service.ProductService;
import com.example.kimthanhphatmvc.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductTypeService productTypeService;

    @Autowired
    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             BrandService brandService,
                             ProductTypeService productTypeService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.productTypeService = productTypeService;
    }

    /** 🟢 Danh sách sản phẩm (lọc bằng slug category.html/brand.html/product_type.html nếu có) */
    @GetMapping
    public String listProducts(
            @RequestParam(value = "category", required = false) String categorySlug,
            @RequestParam(value = "brand", required = false) String brandSlug,
            @RequestParam(value = "productType", required = false) String productTypeSlug,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        Long categoryId = null;
        Long brandId = null;
        Long productTypeId = null;

        if (categorySlug != null) {
            categoryId = categoryService.findBySlug(categorySlug).map(c -> c.getId()).orElse(null);
        }

        if (brandSlug != null) {
            brandId = brandService.findBySlug(brandSlug).map(b -> b.getId()).orElse(null);
        }

        if (productTypeSlug != null) {
            productTypeId = productTypeService.findBySlug(productTypeSlug).map(t -> t.getId()).orElse(null);
        }

        int size = 8;
        Page<Product> productPage = productService.findFiltered(categoryId, brandId, productTypeId, page, size);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        // ✅ Thêm danh sách các bộ lọc
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("productTypes", productTypeService.findAll());

        // ✅ Giữ lại lựa chọn đang chọn
        model.addAttribute("selectedCategory", categorySlug);
        model.addAttribute("selectedBrand", brandSlug);
        model.addAttribute("selectedProductType", productTypeSlug);

        return "public/product_list";
    }

    /** 🟡 Chi tiết sản phẩm bằng slug */
    @GetMapping("/{slug}")
    public String viewProductDetail(@PathVariable String slug, Model model) {
        Product product = productService.findBySlug(slug).orElse(null);
        if (product == null) {
            return "redirect:/products";
        }

        List<Product> relatedProducts = productService.findRelated(product.getCategory().getId(), product.getId());

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);
        return "public/product_detail";
    }

    /** ✅ Hiển thị sản phẩm theo danh mục (khi click dropdown) */
    @GetMapping("/category/{id}")
    public String viewByCategory(
            @PathVariable("id") Long categoryId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        int size = 8;

        Page<Product> productPage = productService.findFiltered(categoryId, null, null, page, size);
        var category = categoryService.findById(categoryId);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("productTypes", productTypeService.findAll());
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedBrand", null);
        model.addAttribute("currentCategory", category);

        return "public/product_list";
    }
}
