package com.example.kimthanhphatmvc.controller;

import com.example.kimthanhphatmvc.service.ContactRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.regex.Pattern;

@Controller
public class ContactController {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+0-9][0-9 .()-]{7,24}$");

    private final ContactRequestService contactRequestService;

    public ContactController(ContactRequestService contactRequestService) {
        this.contactRequestService = contactRequestService;
    }

    @PostMapping("/contact/submit")
    public String submit(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam(required = false, defaultValue = "") String subject,
            @RequestParam String message,
            @RequestParam(required = false, defaultValue = "") String website,
            RedirectAttributes redirectAttributes) {

        if (StringUtils.hasText(website)) {
            redirectAttributes.addFlashAttribute("contactSuccess",
                    "Cảm ơn bạn. Chúng tôi đã nhận được thông tin và sẽ liên hệ sớm.");
            return "redirect:/contact#contact-form";
        }

        String validationError = validate(name, email, phone, subject, message);
        if (validationError != null) {
            redirectAttributes.addFlashAttribute("contactError", validationError);
            preserveForm(redirectAttributes, name, email, phone, subject, message);
            return "redirect:/contact#contact-form";
        }

        try {
            contactRequestService.create(name, email, phone, subject, message);
            redirectAttributes.addFlashAttribute("contactSuccess",
                    "Cảm ơn bạn. Chúng tôi đã nhận được thông tin và sẽ liên hệ sớm.");
        } catch (RuntimeException exception) {
            redirectAttributes.addFlashAttribute("contactError",
                    "Hiện chưa thể ghi nhận yêu cầu. Vui lòng gọi 0934 752 114 để được hỗ trợ.");
            preserveForm(redirectAttributes, name, email, phone, subject, message);
        }

        return "redirect:/contact#contact-form";
    }

    private String validate(String name, String email, String phone, String subject, String message) {
        if (!hasLength(name, 2, 120)) {
            return "Vui lòng nhập họ tên từ 2 đến 120 ký tự.";
        }
        if (!StringUtils.hasText(email) || email.length() > 160 || !EMAIL_PATTERN.matcher(email.strip()).matches()) {
            return "Vui lòng nhập địa chỉ email hợp lệ.";
        }
        if (!StringUtils.hasText(phone) || phone.length() > 30 || !PHONE_PATTERN.matcher(phone.strip()).matches()) {
            return "Vui lòng nhập số điện thoại hợp lệ.";
        }
        if (subject != null && subject.length() > 200) {
            return "Chủ đề không được vượt quá 200 ký tự.";
        }
        if (!hasLength(message, 10, 5000)) {
            return "Nội dung cần có từ 10 đến 5.000 ký tự.";
        }
        return null;
    }

    private boolean hasLength(String value, int min, int max) {
        if (!StringUtils.hasText(value)) {
            return false;
        }
        int length = value.strip().length();
        return length >= min && length <= max;
    }

    private void preserveForm(RedirectAttributes attributes, String name, String email,
                              String phone, String subject, String message) {
        attributes.addFlashAttribute("contactName", name);
        attributes.addFlashAttribute("contactEmail", email);
        attributes.addFlashAttribute("contactPhone", phone);
        attributes.addFlashAttribute("contactSubject", subject);
        attributes.addFlashAttribute("contactMessage", message);
    }
}
