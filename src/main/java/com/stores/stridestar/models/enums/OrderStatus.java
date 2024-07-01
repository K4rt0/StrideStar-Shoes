package com.stores.stridestar.models.enums;

public enum OrderStatus {
    PENDING("Đang chờ xem xét"),
    CONFIRMED("Đã được duyệt"),
    SHIPPING("Đang vận chuyển"),
    DELIVERED("Đã giao hàng"),
    CANCELLED("Đã hủy");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
