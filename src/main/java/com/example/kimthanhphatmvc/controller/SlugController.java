package com.example.kimthanhphatmvc.controller;

import com.example.kimthanhphatmvc.model.*;
import com.example.kimthanhphatmvc.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class SlugController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductTypeService productTypeService;

    public SlugController(ProductService productService,
                          CategoryService categoryService,
                          BrandService brandService,
                          ProductTypeService productTypeService) {

        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.productTypeService = productTypeService;
    }

    @GetMapping("/{slug:[a-z0-9\\-]+}")
    public String handleSlug(@PathVariable String slug, Model model) {

        // 🟢 Ưu tiên kiểm tra PRODUCT trước (vì slug hay trùng)
        Optional<Product> product = productService.findBySlug(slug);
        if (product.isPresent()) {
            Product p = product.get();

            model.addAttribute("product", p);
            model.addAttribute("relatedProducts", productService.findRelatedProducts(p));
            addSidebar(model);

            return "public/product_detail";
        }

        // 🟢 PRODUCT TYPE
        Optional<ProductType> type = productTypeService.findBySlug(slug);
        if (type.isPresent()) {

            ProductType pt = type.get();
            model.addAttribute("productType", pt);
            model.addAttribute("products", pt.getProducts());
            addSidebar(model);

            return "public/product_type";
        }

        // 🟢 BRAND
        Optional<Brand> brand = brandService.findBySlug(slug);
        if (brand.isPresent()) {

            Brand b = brand.get();
            model.addAttribute("brandItem", b);
            model.addAttribute("products", b.getProducts());
            addSidebar(model);

            return "public/brand";
        }

        // 🟢 CATEGORY
        Optional<Category> category = categoryService.findBySlug(slug);
        if (category.isPresent()) {

            Category c = category.get();
            model.addAttribute("category", c);
            model.addAttribute("products", c.getProducts());
            addSidebar(model);

            return "public/category";
        }

        return "error/404";
    }

    /**
     * 🟢 GỬI DỮ LIỆU SIDEBAR DÙNG CHUNG CHO TẤT CẢ TRANG
     */
    private void addSidebar(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brandList", brandService.findAll());
        model.addAttribute("productTypeList", productTypeService.findAll());
    }
}
