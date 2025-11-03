package com.example.kimthanhphatmvc.controller.admin;

import com.example.kimthanhphatmvc.model.Brand;
import com.example.kimthanhphatmvc.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/brands")
public class BrandAdminController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/save")
    public String saveBrand(@RequestParam("name") String name,
                            RedirectAttributes redirectAttributes) {
        try {
            Brand brand = new Brand();
            brand.setName(name);
            brandService.save(brand);

            redirectAttributes.addFlashAttribute("message", "✅ Thêm thương hiệu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "❌ Lỗi khi thêm thương hiệu!");
        }
        return "redirect:/admin/products";
    }
}
