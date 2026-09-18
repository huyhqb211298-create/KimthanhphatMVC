package com.example.kimthanhphatmvc.controller.admin;

import com.example.kimthanhphatmvc.model.enums.ContactStatus;
import com.example.kimthanhphatmvc.service.ContactRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/contacts")
public class ContactAdminController {

    private final ContactRequestService contactRequestService;

    public ContactAdminController(ContactRequestService contactRequestService) {
        this.contactRequestService = contactRequestService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(required = false) ContactStatus status,
                       Model model) {
        model.addAttribute("contactPage", contactRequestService.findPage(page, status));
        model.addAttribute("statuses", ContactStatus.values());
        model.addAttribute("selectedStatus", status);
        return "admin/contact/list";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam ContactStatus status,
                               RedirectAttributes redirectAttributes) {
        if (contactRequestService.updateStatus(id, status)) {
            redirectAttributes.addFlashAttribute("message", "Đã cập nhật trạng thái yêu cầu #" + id + ".");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy yêu cầu liên hệ.");
        }
        return "redirect:/admin/contacts";
    }
}
