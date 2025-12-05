package com.example.kimthanhphatmvc.controller;

import com.example.kimthanhphatmvc.model.*;
import com.example.kimthanhphatmvc.service.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    /**
     * MAIN SLUG HANDLER
     */
    @GetMapping("/{slug:[a-z0-9\\-]+}")
    public String handleSlug(
            @PathVariable String slug,
            @RequestParam(name = "page", defaultValue = "1") int page,
            Model model
    ) {

        int size = 9;

        // PRODUCT
        Optional<Product> product = productService.findBySlug(slug);
        if (product.isPresent()) {

            Product p = product.get();

            model.addAttribute("product", p);
            model.addAttribute("relatedProducts", productService.findRelatedProducts(p));

            addSidebar(model);
            addFilterDefaults(model);

            return "public/product_detail";
        }

        // PRODUCT TYPE
        Optional<ProductType> type = productTypeService.findBySlug(slug);
        if (type.isPresent()) {

            ProductType pt = type.get();

            Page<Product> productPage =
                    productService.findByProductTypePaged(pt.getId(), page, size);

            model.addAttribute("productType", pt);
            model.addAttribute("products", productPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());

            addSidebar(model);
            addFilterDefaults(model);

            return "public/product_type";
        }

        // BRAND
        Optional<Brand> brand = brandService.findBySlug(slug);
        if (brand.isPresent()) {

            Brand b = brand.get();

            Page<Product> productPage =
                    productService.findByBrandPaged(b.getId(), page, size);

            model.addAttribute("brandItem", b);
            model.addAttribute("products", productPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());

            addSidebar(model);
            addFilterDefaults(model);

            return "public/brand";
        }

        // CATEGORY
        Optional<Category> category = categoryService.findBySlug(slug);
        if (category.isPresent()) {

            Category c = category.get();

            Page<Product> productPage =
                    productService.findByCategoryPaged(c.getId(), page, size);

            model.addAttribute("category", c);
            model.addAttribute("products", productPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", productPage.getTotalPages());

            addSidebar(model);
            addFilterDefaults(model);

            return "public/category";
        }

        return "error/404";
    }

    /** Add full sidebar */
    private void addSidebar(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brandList", brandService.findAll());
        model.addAttribute("productTypeList", productTypeService.findAll());
    }

    /** FIX filter for all slug pages */
    private void addFilterDefaults(Model model) {
        model.addAttribute("selectedCategory", null);
        model.addAttribute("selectedBrand", null);
        model.addAttribute("selectedType", null);
        model.addAttribute("keyword", null);
    }
}
