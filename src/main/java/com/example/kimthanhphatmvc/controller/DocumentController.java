package com.example.kimthanhphatmvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
public class DocumentController {

    private static final Set<String> DOCUMENT_SLUGS = Set.of(
            "quy-dinh-thiet-ke-pccc",
            "thu-tuc-phe-duyet-phuong-an-pccc",
            "ho-so-tham-duyet-pccc",
            "ho-so-nghiem-thu-pccc",
            "thong-tu-47",
            "thong-tu-66-2014",
            "qcvn-08-2009",
            "qcvn-12-2014"
    );

    // 🧾 Trang danh sách văn bản
    @GetMapping("/documents")
    public String documentsList() {
        // trỏ tới templates/documents.html
        return "documents";
    }

    // 📘 Trang chi tiết động
    @GetMapping("/documents/{slug}")
    public String documentDetail(@PathVariable("slug") String slug) {
        if (!DOCUMENT_SLUGS.contains(slug)) {
            throw new ResponseStatusException(NOT_FOUND, "Document not found");
        }
        return "documents/" + slug;
    }
}
