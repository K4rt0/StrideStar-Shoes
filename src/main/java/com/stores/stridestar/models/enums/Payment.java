package com.stores.stridestar.models.enums;

public enum Payment {
    COD("Tiền mặt"),
    VNPAY("VnPay"),
    ZALO("ZaloPay"),
    VISA("Visa"),
    MASTER("MasterCard");

    private final String value;

    Payment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
