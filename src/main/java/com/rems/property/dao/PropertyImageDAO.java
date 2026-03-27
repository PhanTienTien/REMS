package com.rems.property.dao;

import com.rems.property.model.PropertyImage;

import java.sql.Connection;
import java.util.List;

public interface PropertyImageDAO {

    List<PropertyImage> findByPropertyId(Connection conn,
                                         Long propertyId);

    void insert(Connection conn,
                Long propertyId,
                String imageUrl);

    String getThumbnail(Connection conn, Long propertyId);

    void insertMultiple(Connection conn, Long propertyId, List<String> imageUrls);

    void deleteById(Connection conn, Long id);

    void resetThumbnail(Connection conn, Long propertyId);
}
