package com.example.be_kiotviet.entity;

public enum BusinessType {
    CAFE("Quán cà phê / trà sữa"),
    FASHION("Thời trang / giày dép"),
    ELECTRONICS("Điện máy / máy tính"),
    GROCERY("Tạp hóa / siêu thị mini"),
    RESTAURANT("Nhà hàng / quán ăn"),
    BEAUTY("Mỹ phẩm / spa / salon"),
    PHARMACY("Nhà thuốc"),
    OTHER("Khác"); // ← Giải pháp mặc định cho mọi trường hợp đặc biệt

    private final String description;

    BusinessType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
