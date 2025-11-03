package com.example.kimthanhphatmvc.dto;

import com.example.kimthanhphatmvc.model.enums.PriceDisplay;
import com.example.kimthanhphatmvc.model.enums.StockStatus;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private MultipartFile image;
    private String imageUrl;
    private String origin;
    private Integer warrantyMonths;
    private StockStatus stockStatus;
    private PriceDisplay priceDisplay;
    private Long categoryId;
    private Long brandId;
}
