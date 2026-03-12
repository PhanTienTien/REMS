package com.rems.property.service;

import com.rems.property.model.PropertyImage;

import java.util.List;

public interface PropertyImageService {

    List<PropertyImage> getByPropertyId(Long propertyId);

    String getThumbnail(Long propertyId);

    void addImages(Long propertyId, List<String> imageUrls);

}