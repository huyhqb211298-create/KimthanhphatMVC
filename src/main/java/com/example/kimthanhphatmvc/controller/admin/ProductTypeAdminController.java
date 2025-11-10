package com.example.kimthanhphatmvc.controller.admin;

import com.example.kimthanhphatmvc.model.ProductType;
import com.example.kimthanhphatmvc.repository.ProductTypeRepository;
import com.example.kimthanhphatmvc.service.ProductTypeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/product-types")
public class ProductTypeAdminController {

    private final ProductTypeService productTypeService;

    public ProductTypeAdminController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @PostMapping("/save")
    public String saveProductType(@RequestParam("name") String name, RedirectAttributes redirectAttributes) {
        try {
            if (productTypeService.existsByName(name)) {
                redirectAttributes.addFlashAttribute("message", "⚠️ Loại sản phẩm \"" + name + "\" đã tồn tại!");
                return "redirect:/admin/products";
            }

            ProductType productType = new ProductType();
            productType.setName(name);
            productTypeService.save(productType);

            redirectAttributes.addFlashAttribute("message", "✅ Thêm loại sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "❌ Lỗi khi thêm loại sản phẩm!");
        }
        return "redirect:/admin/products";
    }
}
