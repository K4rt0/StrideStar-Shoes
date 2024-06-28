package com.stores.stridestar.access;

import lombok.AllArgsConstructor;

public enum Provider {
    LOCAL("Local"),
    GOOGLE("Google");
    public final String value;

    Provider(String value) {
        this.value = value;
    }
}
