package com.example.kimthanhphatmvc.model.enums;

public enum StockStatus {
    IN_STOCK("Còn hàng"),
    OUT_OF_STOCK("Hết hàng");

    private final String label;

    StockStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
