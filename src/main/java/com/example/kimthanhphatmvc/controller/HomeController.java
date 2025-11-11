package com.example.kimthanhphatmvc.controller;

import com.example.kimthanhphatmvc.model.Category;
import com.example.kimthanhphatmvc.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Controller
public class HomeController {

    private final CategoryService categoryService;

    // ✅ Spring tự inject CategoryServiceImpl vào đây
    public HomeController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories != null ? categories : Collections.emptyList());
        return "index";
    }
    @GetMapping("/hsnl")
    public String hoSo() {
        return "hsnl"; // tên file trong /templates/hsnl.html
    }
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
    @GetMapping("/about")
    public String about() {
        return "about"; // Template: about.html
    }

    @GetMapping("/projects")
    public String projects() {
        return "projects"; // Template: projects.html
    }

    @GetMapping("/consulting")
    public String consulting() {
        return "consulting"; // Template: consulting.html
    }

    @GetMapping("/news")
    public String news() {
        return "news"; // Template: news.html
    }

    @GetMapping("/laws")
    public String laws() {
        return "laws"; // Template: laws.html
    }

}
