package com.example.kimthanhphatmvc.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsDTO {

    private Long id;

    private String title;

    private String slug;
    private String summary;

    private String thumbnail; // URL ảnh cũ

    private MultipartFile thumbnailFile; // ảnh upload mới

    private String content;

    private Boolean active;
}
