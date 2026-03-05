package com.rems.property.dao.impl;

import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;
import com.rems.property.dao.PropertyDAO;
import com.rems.property.model.Property;
import com.rems.common.constant.PropertyStatus;
import com.rems.common.constant.PropertyType;
import com.rems.common.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class PropertyDAOImpl implements PropertyDAO {

    private Property map(ResultSet rs) throws SQLException {

        Property p = new Property();

        p.setId(rs.getLong("id"));
        p.setTitle(rs.getString("title"));
        p.setAddress(rs.getString("address"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));

        p.setType(PropertyType.valueOf(rs.getString("type")));
        p.setStatus(PropertyStatus.valueOf(rs.getString("status")));

        Timestamp approvedAt = rs.getTimestamp("approved_at");
        if (approvedAt != null) {
            p.setApprovedAt(approvedAt.toLocalDateTime());
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            p.setCreatedAt(createdAt.toLocalDateTime());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            p.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        p.setCreatedBy(rs.getLong("created_by"));
        p.setUpdatedBy(rs.getObject("updated_by", Long.class));
        p.setApprovedBy(rs.getLong("approved_by"));

        return p;
    }

    @Override
    public Optional<Property> findById(Long id) {

        String sql = "SELECT * FROM properties WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(map(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Property> findByIdForUpdate(Connection conn, Long id) {

        String sql = "SELECT * FROM properties WHERE id = ? FOR UPDATE";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Optional.of(map(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStatus(Connection conn,
                             Long id,
                             PropertyStatus status) {

        String sql = """
        UPDATE properties
        SET status = ?, 
            updated_at = NOW()
        WHERE id = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setLong(2, id);

            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new BusinessException(ErrorCode.NOT_FOUND);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update property status", e);
        }
    }

    @Override
    public void updateApproval(Connection conn,
                               Long id,
                               LocalDateTime approvedAt) {

        String sql = """
        UPDATE properties
        SET approved_at = ?, 
            updated_at = NOW()
        WHERE id = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(approvedAt));
            ps.setLong(2, id);

            int affected = ps.executeUpdate();

            if (affected == 0) {
                throw new BusinessException(ErrorCode.NOT_FOUND);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to update property approval", e);
        }
    }

    //test
    public Long insertTestProperty(Connection conn,
                                   String title,
                                   BigDecimal price) {

        String sql = """
        INSERT INTO properties
        (
            title,
            address,
            description,
            price,
            type,
            status,
            approved_at,
            approved_by,
            created_by,
            updated_by,
            created_at,
            updated_at
        )
        VALUES (?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?, NOW(), NULL)
    """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, title);
            ps.setString(2, "Test Address Hanoi");
            ps.setString(3, "Integration test property");

            ps.setBigDecimal(4, price);

            ps.setString(5, "SALE");       // type enum
            ps.setString(6, "AVAILABLE");  // status enum

            ps.setLong(7, 2L); // approved_by (staff)
            ps.setLong(8, 2L); // created_by (staff)

            ps.setObject(9, null); // updated_by

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("Insert property failed");
    }

    public String getPropertyStatus(Connection conn, Long propertyId) {

        String sql = "SELECT status FROM properties WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, propertyId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("status");
            }

            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



//    @Override
//    public List<Property> search(String address,
//                                 PropertyType type,
//                                 BigDecimal minPrice,
//                                 BigDecimal maxPrice) {
//
//        StringBuilder sql = new StringBuilder(
//                "SELECT * FROM properties WHERE status = 'AVAILABLE' "
//        );
//
//        List<Object> params = new ArrayList<>();
//
//        if (address != null && !address.isBlank()) {
//            sql.append(" AND address LIKE ?");
//            params.add("%" + address + "%");
//        }
//
//        if (type != null) {
//            sql.append(" AND type = ?");
//            params.add(type.name());
//        }
//
//        if (minPrice != null) {
//            sql.append(" AND price >= ?");
//            params.add(minPrice);
//        }
//
//        if (maxPrice != null) {
//            sql.append(" AND price <= ?");
//            params.add(maxPrice);
//        }
//
//        try (Connection conn = DBUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
//
//            for (int i = 0; i < params.size(); i++) {
//                ps.setObject(i + 1, params.get(i));
//            }
//
//            ResultSet rs = ps.executeQuery();
//
//            List<Property> result = new ArrayList<>();
//            while (rs.next()) {
//                result.add(map(rs));
//            }
//
//            return result;
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public Map<PropertyStatus, Long> countByStatus() {
//
//        String sql = """
//            SELECT status, COUNT(*) as total
//            FROM properties
//            GROUP BY status
//        """;
//
//        try (Connection conn = DBUtil.getConnection();
//             PreparedStatement ps = conn.prepareStatement(sql);
//             ResultSet rs = ps.executeQuery()) {
//
//            Map<PropertyStatus, Long> map = new EnumMap<>(PropertyStatus.class);
//
//            while (rs.next()) {
//                PropertyStatus status =
//                        PropertyStatus.valueOf(rs.getString("status"));
//                map.put(status, rs.getLong("total"));
//            }
//
//            return map;
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
