package com.example.kimthanhphatmvc.controller;

import com.example.kimthanhphatmvc.model.Product;
import com.example.kimthanhphatmvc.service.BrandService;
import com.example.kimthanhphatmvc.service.CategoryService;
import com.example.kimthanhphatmvc.service.ProductService;
import com.example.kimthanhphatmvc.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

    /** 🟢 Danh sách sản phẩm (lọc + tìm kiếm) */
    @GetMapping({"", "/", " "})
    public String listProducts(
            @RequestParam(value = "category", required = false) String categorySlug,
            @RequestParam(value = "brand", required = false) String brandSlug,
            @RequestParam(value = "productType", required = false) String productTypeSlug,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        Long categoryId = categorySlug == null ? null :
                categoryService.findBySlug(categorySlug).map(c -> c.getId()).orElse(null);

        Long brandId = brandSlug == null ? null :
                brandService.findBySlug(brandSlug).map(b -> b.getId()).orElse(null);

        Long productTypeId = productTypeSlug == null ? null :
                productTypeService.findBySlug(productTypeSlug).map(t -> t.getId()).orElse(null);

        int size = 8;

        Page<Product> productPage = productService.findFiltered(
                categoryId,
                brandId,
                productTypeId,
                keyword,
                page,
                size
        );

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());

        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("productTypes", productTypeService.findAll());

        model.addAttribute("selectedCategory", categorySlug);
        model.addAttribute("selectedBrand", brandSlug);
        model.addAttribute("selectedProductType", productTypeSlug);
        model.addAttribute("keyword", keyword);

        return "public/product_list";
    }

    /** 🟡 Chi tiết sản phẩm bằng slug */
    @Transactional(readOnly = true)
    @GetMapping("/{slug}")
    public String viewProductDetail(@PathVariable String slug, Model model) {

        Product product = productService.findBySlug(slug).orElse(null);
        if (product == null) {
            return "redirect:/products";
        }

        List<Product> relatedProducts = productService.findRelatedProducts(product);

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);

        return "public/product_detail";
    }

    /** ✅ Hiển thị sản phẩm theo danh mục */
    @GetMapping("/category/{id}")
    public String viewByCategory(
            @PathVariable("id") Long categoryId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        int size = 8;

        Page<Product> productPage = productService.findFiltered(categoryId, null, null, null, page, size);
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
