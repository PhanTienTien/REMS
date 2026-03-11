package com.rems.property.dao;

import com.rems.common.constant.PropertyStatus;
import com.rems.common.constant.PropertyType;
import com.rems.property.model.Property;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PropertyDAO {

    Optional<Property> findById(Long id);

    Optional<Property> findByIdForUpdate(Connection conn, Long id);

    void updateStatus(Connection conn,
                      Long id,
                      PropertyStatus status);

    void updateApproval(Connection conn,
                        Long id,
                        LocalDateTime approvedAt);

    Long insert(Connection conn, Property property);

    void update(Connection conn, Property property);

    List<Property> findAll();

    List<Property> search(
            String address,
            PropertyType type,
            BigDecimal minPrice,
            BigDecimal maxPrice
    );

    List<Property> findByStatus(PropertyStatus status);
}
