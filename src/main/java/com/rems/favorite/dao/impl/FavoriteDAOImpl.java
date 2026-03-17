package com.rems.favorite.dao.impl;

import com.rems.favorite.dao.FavoriteDAO;
import com.rems.property.model.Property;

import java.sql.*;
import java.util.List;

public class FavoriteDAOImpl implements FavoriteDAO {
    @Override
    public Long insert(Connection conn,
                       Long customerId,
                       Long propertyId) {

        String sql = """
        INSERT INTO favorites(customer_id, property_id)
        VALUES (?, ?)
        """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, customerId);
            ps.setLong(2, propertyId);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public boolean exists(Connection conn, Long customerId, Long propertyId) {
        return false;
    }

    @Override
    public void delete(Connection conn, Long customerId, Long propertyId) {

    }

    @Override
    public List<Property> findByCustomer(Connection conn, Long customerId) {
        return List.of();
    }
}
