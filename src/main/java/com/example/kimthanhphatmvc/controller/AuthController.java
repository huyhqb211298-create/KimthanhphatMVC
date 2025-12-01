package com.example.kimthanhphatmvc.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    // ==========================
    //  TRANG LOGIN
    // ==========================
    @GetMapping("/login")
    public String login(Authentication auth) {

        // Nếu đã đăng nhập → không cho vào login nữa
        if (auth != null && auth.isAuthenticated()) {

            boolean isAdmin = auth.getAuthorities()
                    .stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                return "redirect:/admin";
            } else {
                return "redirect:/";
            }
        }

        return "login"; // file login.html
    }

    // ==========================
    //  REDIRECT SAU KHI LOGIN
    // ==========================
    @GetMapping("/redirect-by-role")
    public String redirectByRole(Authentication authentication) {

        if (authentication == null) {
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return "redirect:/admin";
        }

        return "redirect:/";
    }
}
