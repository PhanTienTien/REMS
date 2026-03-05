package com.rems.property.model.dto;

import com.rems.common.constant.PropertyType;
import java.math.BigDecimal;

public class CreatePropertyDTO {

    private String title;
    private String address;
    private PropertyType type;
    private BigDecimal price;

    public CreatePropertyDTO() {}

    public CreatePropertyDTO(String title, String address, PropertyType type, BigDecimal price) {
        this.title = title;
        this.address = address;
        this.type = type;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public PropertyType getType() {
        return type;
    }
    public void setType(PropertyType type) {
        this.type = type;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}