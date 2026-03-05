package com.rems.property.dao;

import com.rems.common.constant.PropertyStatus;
import com.rems.common.constant.PropertyType;
import com.rems.property.model.Property;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    //test
    Long insertTestProperty(Connection conn, String title, BigDecimal price);
    String getPropertyStatus(Connection conn, Long propertyId);
}
