package com.example.kimthanhphatmvc.controller.admin;

import com.example.kimthanhphatmvc.dto.ProductDTO;
import com.example.kimthanhphatmvc.model.Product;
import com.example.kimthanhphatmvc.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final CloudinaryService cloudinaryService;
    private final ProductTypeService productTypeService; // 🔹 Thêm service cho loại sản phẩm

    @Autowired
    public ProductAdminController(ProductService productService,
                                  CategoryService categoryService,
                                  BrandService brandService,
                                  CloudinaryService cloudinaryService,
                                  ProductTypeService productTypeService) { // 🔹 Inject thêm ProductTypeService
        this.productService = productService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.cloudinaryService = cloudinaryService;
        this.productTypeService = productTypeService;
    }

    // ===== Danh sách sản phẩm =====
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/product/list";
    }

    // ===== Form thêm mới =====
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("productTypes", productTypeService.findAll()); // 🔹 Thêm danh sách loại sản phẩm
        model.addAttribute("isEdit", false);
        return "admin/product/form";
    }

    // ===== Form sửa =====
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Product product = productService.findById(id);
        if (product == null) {
            ra.addFlashAttribute("message", "❌ Sản phẩm không tồn tại.");
            return "redirect:/admin/products";
        }

        ProductDTO dto = mapToDTO(product);
        model.addAttribute("product", dto);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("productTypes", productTypeService.findAll()); // 🔹 Thêm danh sách loại sản phẩm
        model.addAttribute("isEdit", true);
        return "admin/product/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("product") ProductDTO dto,
                       RedirectAttributes ra) {

        try {
            Product entity = (dto.getId() != null)
                    ? productService.findById(dto.getId())
                    : new Product();

            if (entity == null) entity = new Product();

            // --- Gán dữ liệu cơ bản ---
            entity.setName(dto.getName());
            entity.setPrice(dto.getPrice());
            entity.setDescription(dto.getDescription());
            entity.setOrigin(dto.getOrigin());
            entity.setWarrantyMonths(dto.getWarrantyMonths());
            entity.setStockStatus(dto.getStockStatus());
            entity.setPriceDisplay(dto.getPriceDisplay());

            // --- Gán Brand / Category ---
            entity.setBrand(dto.getBrandId() != null ? brandService.findById(dto.getBrandId()) : null);
            entity.setCategory(dto.getCategoryId() != null
                    ? categoryService.findById(dto.getCategoryId()).orElse(null)
                    : null);

            // --- Gán ProductType ---
            entity.setProductType(dto.getProductTypeId() != null
                    ? productTypeService.findById(dto.getProductTypeId()).orElse(null)
                    : null);

            // --- Upload hình lên Cloudinary ---
            MultipartFile file = dto.getImage();
            if (file != null && !file.isEmpty()) {
                String imageUrl = cloudinaryService.uploadFile(file);
                entity.setImage(imageUrl);
            } else if (dto.getId() != null) {
                Product old = productService.findById(dto.getId());
                if (old != null) entity.setImage(old.getImage());
            }

            productService.save(entity);
            ra.addFlashAttribute("message", "✅ Lưu sản phẩm thành công!");

        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("message", "❌ Lỗi khi lưu sản phẩm: " + e.getMessage());
        }

        return "redirect:/admin/products";
    }

    // ===== Xóa =====
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            productService.deleteById(id);
            ra.addFlashAttribute("message", "🗑️ Xóa sản phẩm thành công.");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("message", "❌ Không thể xóa sản phẩm.");
        }
        return "redirect:/admin/products";
    }

    // ===== API JSON =====
    @GetMapping("/api/{id}")
    @ResponseBody
    public Product getProductJson(@PathVariable Long id) {
        return productService.findById(id);
    }

    // ---- Helper: map entity -> dto ----
    private ProductDTO mapToDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setPrice(p.getPrice());
        dto.setDescription(p.getDescription());
        dto.setOrigin(p.getOrigin());
        dto.setWarrantyMonths(p.getWarrantyMonths());
        dto.setStockStatus(p.getStockStatus());
        dto.setPriceDisplay(p.getPriceDisplay());
        if (p.getCategory() != null) dto.setCategoryId(p.getCategory().getId());
        if (p.getBrand() != null) dto.setBrandId(p.getBrand().getId());
        if (p.getProductType() != null) dto.setProductTypeId(p.getProductType().getId()); // 🔹 map thêm
        dto.setImageUrl(p.getImage());
        return dto;
    }
}
