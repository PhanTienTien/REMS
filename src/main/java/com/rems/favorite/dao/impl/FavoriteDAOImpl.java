package com.rems.favorite.dao.impl;

import com.rems.favorite.dao.FavoriteDAO;
import com.rems.property.dto.PropertyCardDTO;

import java.sql.*;
import java.util.ArrayList;
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
    public boolean exists(Connection conn,
                          Long customerId,
                          Long propertyId) {

        String sql = """
        SELECT 1 FROM favorites
        WHERE customer_id = ? AND property_id = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, customerId);
            ps.setLong(2, propertyId);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Connection conn, Long customerId, Long propertyId) {

    }

    @Override
    public List<PropertyCardDTO> findByCustomer(Connection conn, Long customerId) {

        String sql = """
        SELECT p.id, p.title, p.address, p.price, p.type,
               pi.image_url AS thumbnail
        FROM favorites f
        JOIN properties p ON f.property_id = p.id
        LEFT JOIN property_images pi 
            ON pi.property_id = p.id AND pi.is_thumbnail = 1
        WHERE f.customer_id = ?
        ORDER BY f.created_at DESC
    """;

        List<PropertyCardDTO> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, customerId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                PropertyCardDTO dto = new PropertyCardDTO();

                dto.setId(rs.getLong("id"));
                dto.setTitle(rs.getString("title"));
                dto.setAddress(rs.getString("address"));
                dto.setPrice(rs.getBigDecimal("price"));
                dto.setType(rs.getString("type"));
                dto.setThumbnail(rs.getString("thumbnail"));

                list.add(dto);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
