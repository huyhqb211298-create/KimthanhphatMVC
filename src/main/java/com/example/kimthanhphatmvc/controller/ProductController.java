package com.example.kimthanhphatmvc.controller;

import com.example.kimthanhphatmvc.model.Product;
import com.example.kimthanhphatmvc.service.BrandService;
import com.example.kimthanhphatmvc.service.CategoryService;
import com.example.kimthanhphatmvc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;

    @Autowired
    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             BrandService brandService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
    }

    /** 🟢 Danh sách sản phẩm (lọc bằng slug category/brand nếu có) */
    @GetMapping
    public String listProducts(
            @RequestParam(value = "category", required = false) String categorySlug,
            @RequestParam(value = "brand", required = false) String brandSlug,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        Long categoryId = null;
        Long brandId = null;

        if (categorySlug != null) {
            categoryId = categoryService.findBySlug(categorySlug).map(c -> c.getId()).orElse(null);
        }

        if (brandSlug != null) {
            brandId = brandService.findBySlug(brandSlug).map(b -> b.getId()).orElse(null);
        }

        int size = 8;
        Page<Product> productPage = productService.findFiltered(categoryId, brandId, page, size);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("selectedCategory", categorySlug);
        model.addAttribute("selectedBrand", brandSlug);

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

    // ✅ Hiển thị sản phẩm theo danh mục (ví dụ khi click dropdown)
    @GetMapping("/category/{id}")
    public String viewByCategory(
            @PathVariable("id") Long categoryId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {

        int size = 8;

        // Lọc sản phẩm theo danh mục
        Page<Product> productPage = productService.findFiltered(categoryId, null, page, size);

        // Lấy thông tin danh mục hiện tại
        var category = categoryService.findById(categoryId);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("selectedBrand", null);
        model.addAttribute("currentCategory", category);

        return "public/product_list"; // dùng lại giao diện danh sách chung
    }

}
