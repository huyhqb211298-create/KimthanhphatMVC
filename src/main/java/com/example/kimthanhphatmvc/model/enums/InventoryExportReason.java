package com.example.kimthanhphatmvc.model.enums;

public enum InventoryExportReason {

    SALE("Xuất bán"),
    DAMAGED("Hư hỏng"),
    ADJUSTMENT("Điều chỉnh kho");

    private final String label;

    InventoryExportReason(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

