package com.rems.common.constant;

public enum PropertyType {

    SALE,
    RENT;

    public boolean isSale() {
        return this == SALE;
    }

    public boolean isRent() {
        return this == RENT;
    }
}
