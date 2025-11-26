package com.example.kimthanhphatmvc.controller;


import com.example.kimthanhphatmvc.service.CategoryService;
import com.example.kimthanhphatmvc.service.BrandService;
import com.example.kimthanhphatmvc.service.ProductTypeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductTypeService productTypeService;

    /** 🟢 TỰ ĐỘNG TRUYỀN DỮ LIỆU HEADER CHO MỌI VIEW */
    @ModelAttribute
    public void addHeaderData(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brandList", brandService.findAll());
        model.addAttribute("productTypeList", productTypeService.findAll());
    }
}
