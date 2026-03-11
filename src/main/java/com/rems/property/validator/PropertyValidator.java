package com.rems.property.validator;

import com.rems.common.constant.PropertyType;

import java.math.BigDecimal;

public class PropertyValidator {

    public static BigDecimal validatePrice(String value, String fieldName) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {

            BigDecimal price = new BigDecimal(value);

            if (price.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException(fieldName + " must be >= 0");
            }

            return price;

        } catch (NumberFormatException e) {
            throw new RuntimeException(fieldName + " must be a valid number");
        }
    }

    public static PropertyType validateType(String typeStr) {

        if (typeStr == null || typeStr.isBlank()) {
            return null;
        }

        try {
            return PropertyType.valueOf(typeStr);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid property type");
        }
    }
}