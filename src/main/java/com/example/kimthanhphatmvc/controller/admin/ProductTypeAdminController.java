package com.example.kimthanhphatmvc.controller.admin;

import com.example.kimthanhphatmvc.model.ProductType;
import com.example.kimthanhphatmvc.service.ProductTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/product-types")
public class ProductTypeAdminController {

    private final ProductTypeService productTypeService;

    public ProductTypeAdminController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    // =============== AJAX ===============
    @PostMapping("/save-ajax")
    @ResponseBody
    public ProductType saveProductTypeAjax(@RequestParam("name") String name) {

        ProductType type = new ProductType();
        type.setName(name);

        return productTypeService.save(type);
    }

    // =============== FORM SUBMIT ===============
    @PostMapping("/save")
    public String saveProductType(@RequestParam("name") String name,
                                  org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            if (productTypeService.existsByName(name)) {
                redirectAttributes.addFlashAttribute("message", "⚠️ Loại sản phẩm \"" + name + "\" đã tồn tại!");
                return "redirect:/admin/products";
            }

            ProductType type = new ProductType();
            type.setName(name);
            productTypeService.save(type);

            redirectAttributes.addFlashAttribute("message", "✅ Thêm loại sản phẩm thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "❌ Lỗi khi thêm loại sản phẩm!");
        }
        return "redirect:/admin/products";
    }
}
