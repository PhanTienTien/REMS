package com.rems.favorite.dao;

import com.rems.property.dto.PropertyCardDTO;

import java.sql.Connection;
import java.util.List;

public interface FavoriteDAO {

    Long insert(Connection conn, Long customerId, Long propertyId);

    boolean exists(Connection conn, Long customerId, Long propertyId);

    void delete(Connection conn, Long customerId, Long propertyId);

    List<PropertyCardDTO> findByCustomer(Connection conn, Long customerId);
}
