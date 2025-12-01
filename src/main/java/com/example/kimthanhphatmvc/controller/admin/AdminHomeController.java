package com.example.kimthanhphatmvc.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {

    @GetMapping("/admin")
    public String adminHome() {
        return "admin/home";  // trả về file admin/home.html
    }
}
