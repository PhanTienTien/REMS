package com.rems.property.dto;

import java.math.BigDecimal;

public class PropertyCardDTO {

    private Long id;
    private String title;
    private String address;
    private BigDecimal price;
    private String type;
    private String thumbnail;

    public PropertyCardDTO() {}

    public PropertyCardDTO(Long id,
                           String title,
                           String address,
                           BigDecimal price,
                           String type,
                           String thumbnail) {
        this.id = id;
        this.title = title;
        this.address = address;
        this.price = price;
        this.type = type;
        this.thumbnail = thumbnail;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}