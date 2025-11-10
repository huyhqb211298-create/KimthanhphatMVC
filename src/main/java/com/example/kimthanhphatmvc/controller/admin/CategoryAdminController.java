package com.example.kimthanhphatmvc.controller.admin;

import com.example.kimthanhphatmvc.model.Category;
import com.example.kimthanhphatmvc.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/save")
    public String saveCategory(@RequestParam("name") String name,
                               RedirectAttributes redirectAttributes) {
        try {
            if (categoryService.existsByName(name)) {
                redirectAttributes.addFlashAttribute("message", "⚠️ Danh mục \"" + name + "\" đã tồn tại!");
                return "redirect:/admin/products";
            }

            Category category = new Category();
            category.setName(name);
            categoryService.save(category);

            redirectAttributes.addFlashAttribute("message", "✅ Thêm danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "❌ Lỗi khi thêm danh mục!");
        }
        return "redirect:/admin/products";
    }
}
