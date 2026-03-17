package com.rems.favorite.dao;

import com.rems.property.model.Property;

import java.sql.Connection;
import java.util.List;

public interface FavoriteDAO {

    Long insert(Connection conn, Long customerId, Long propertyId);

    boolean exists(Connection conn, Long customerId, Long propertyId);

    void delete(Connection conn, Long customerId, Long propertyId);

    List<Property> findByCustomer(Connection conn, Long customerId);
}
