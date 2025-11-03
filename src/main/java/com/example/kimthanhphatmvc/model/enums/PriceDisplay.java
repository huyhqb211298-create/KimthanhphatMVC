package com.example.kimthanhphatmvc.model.enums;

public enum PriceDisplay {
    SHOW("Hiển thị giá"),
    CONTACT("Liên hệ để biết giá");

    private final String label;

    PriceDisplay(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
