package com.example.kimthanhphatmvc.model.enums;

public enum ContactStatus {
    NEW("Mới"),
    CONTACTED("Đã liên hệ"),
    COMPLETED("Hoàn thành");

    private final String displayName;

    ContactStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
