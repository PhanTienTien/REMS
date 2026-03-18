package com.rems.property.service;

import com.rems.property.model.PropertyImage;

import java.sql.Connection;
import java.util.List;

public interface PropertyImageService {

    List<PropertyImage> getByPropertyId(Long propertyId);

    String getThumbnail(Long propertyId);

    void addImages(Connection conn,
                   Long propertyId,
                   List<String> imageUrls);

}