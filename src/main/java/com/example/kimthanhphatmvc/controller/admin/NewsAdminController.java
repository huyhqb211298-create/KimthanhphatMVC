package com.example.kimthanhphatmvc.controller.admin;

import com.example.kimthanhphatmvc.dto.NewsDTO;
import com.example.kimthanhphatmvc.model.News;
import com.example.kimthanhphatmvc.service.CloudinaryService;
import com.example.kimthanhphatmvc.service.NewsService;
import com.example.kimthanhphatmvc.service.SlugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/news")
public class NewsAdminController {

    private final NewsService newsService;
    private final CloudinaryService cloudinaryService;
    private final SlugService slugService;

    @Autowired
    public NewsAdminController(NewsService newsService,
                               CloudinaryService cloudinaryService,
                               SlugService slugService) {
        this.newsService = newsService;
        this.cloudinaryService = cloudinaryService;
        this.slugService = slugService;
    }

    // ===================== LIST ==========================
    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", newsService.findAll());
        return "admin/news/list";
    }

    // ===================== FORM CREATE =====================
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("news", new NewsDTO());
        model.addAttribute("isEdit", false);
        return "admin/news/form";
    }

    // ===================== FORM EDIT =====================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        News news = newsService.findById(id);

        if (news == null) {
            ra.addFlashAttribute("message", "❌ Tin tức không tồn tại!");
            return "redirect:/admin/news";
        }

        // Map News → DTO
        NewsDTO dto = new NewsDTO();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setSlug(news.getSlug());
        dto.setContent(news.getContent());
        dto.setThumbnail(news.getThumbnail());
        dto.setActive(news.getActive());

        model.addAttribute("news", dto);
        model.addAttribute("isEdit", true);

        return "admin/news/form";
    }

    // ===================== SAVE =====================
    @PostMapping("/save")
    public String save(@ModelAttribute("news") NewsDTO dto,
                       RedirectAttributes ra) {

        try {
            MultipartFile file = dto.getThumbnailFile();

            // Nếu tạo mới → slug tự tạo
            if (dto.getId() == null) {
                dto.setSlug(slugService.createSlug(dto.getTitle()));
            }

            // Upload ảnh nếu có
            if (file != null && !file.isEmpty()) {
                String url = cloudinaryService.upload(file);
                dto.setThumbnail(url);
            }

            // Lưu
            if (dto.getId() == null) {
                newsService.save(dto);
                ra.addFlashAttribute("message", "✅ Thêm tin tức thành công!");
            } else {
                newsService.update(dto.getId(), dto);
                ra.addFlashAttribute("message", "✅ Cập nhật tin tức thành công!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("message", "❌ Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/news";
    }

    // ===================== DELETE =====================
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            newsService.delete(id);
            ra.addFlashAttribute("message", "🗑️ Xóa tin tức thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("message", "❌ Không thể xóa tin tức!");
        }
        return "redirect:/admin/news";
    }
}
