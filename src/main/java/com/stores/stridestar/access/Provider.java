package com.stores.stridestar.access;

public enum Provider {
    LOCAL("Local"),
    GOOGLE("Google");
    public final String value;

    Provider(String value) {
        this.value = value;
    }
}
