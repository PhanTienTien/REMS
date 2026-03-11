package com.rems.property.model.dto;

import com.rems.common.constant.PropertyType;
import java.math.BigDecimal;

public class UpdatePropertyDTO {

    private Long id;
    private String title;
    private String address;
    private String description;
    private PropertyType type;
    private BigDecimal price;

    public UpdatePropertyDTO() {}

    public UpdatePropertyDTO(Long id,
                             String title,
                             String address,
                             String description,
                             PropertyType type,
                             BigDecimal price) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.description = description;
        this.type = type;
        this.price = price;
    }

    public void validate() {

        if (id == null) {
            throw new IllegalArgumentException("Property ID is required");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }

        if (type == null) {
            throw new IllegalArgumentException("Property type is required");
        }

        if (price == null) {
            throw new IllegalArgumentException("Price is required");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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