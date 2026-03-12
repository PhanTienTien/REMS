package com.rems.property.dao.impl;

import com.rems.common.constant.PropertyStatus;
import com.rems.common.constant.PropertyType;
import com.rems.common.util.DBUtil;
import com.rems.property.dao.PropertyDAO;
import com.rems.property.dto.PropertyCardDTO;
import com.rems.property.dto.PropertySearchDTO;
import com.rems.property.model.Property;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        p.setApprovedAt(
                rs.getTimestamp("approved_at") != null
                        ? rs.getTimestamp("approved_at").toLocalDateTime()
                        : null
        );

        p.setCreatedBy(rs.getLong("created_by"));
        p.setUpdatedBy(rs.getObject("updated_by") != null
                ? rs.getLong("updated_by")
                : null);

        p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        if (rs.getTimestamp("updated_at") != null) {
            p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }

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

        } catch (Exception e) {
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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Long insert(Connection conn, Property property) {

        String sql = """
        INSERT INTO properties
        (title,address,description,price,type,status,created_by)
        VALUES (?,?,?,?,?,?,?)
    """;

        try (PreparedStatement ps =
                     conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, property.getTitle());
            ps.setString(2, property.getAddress());
            ps.setString(3, property.getDescription());
            ps.setBigDecimal(4, property.getPrice());
            ps.setString(5, property.getType().name());
            ps.setString(6, property.getStatus().name());
            ps.setLong(7, property.getCreatedBy());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getLong(1);
            }

            throw new RuntimeException("Insert failed");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Connection conn, Property property) {

        String sql = """
        UPDATE properties
        SET title=?,
            address=?,
            description=?,
            price=?,
            type=?,
            updated_at=NOW()
        WHERE id=?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, property.getTitle());
            ps.setString(2, property.getAddress());
            ps.setString(3, property.getDescription());
            ps.setBigDecimal(4, property.getPrice());
            ps.setString(5, property.getType().name());
            ps.setLong(6, property.getId());

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStatus(Connection conn, Long id, PropertyStatus status) {

        String sql = "UPDATE properties SET status=? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());
            ps.setLong(2, id);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateApproval(Connection conn,
                               Long id,
                               LocalDateTime approvedAt) {

        String sql = """
        UPDATE properties
        SET approved_at = ?
        WHERE id = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(approvedAt));
            ps.setLong(2, id);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Property> findAll() {

        String sql = "SELECT * FROM properties ORDER BY created_at DESC";

        List<Property> result = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(map(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public List<Property> findByStatus(PropertyStatus status) {

        String sql = "SELECT * FROM properties WHERE status=?";

        List<Property> result = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status.name());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(map(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public List<Property> search(String address,
                                 PropertyType type,
                                 BigDecimal minPrice,
                                 BigDecimal maxPrice) {

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM properties WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if (address != null && !address.isBlank()) {
            sql.append(" AND address LIKE ?");
            params.add("%" + address + "%");
        }

        if (type != null) {
            sql.append(" AND type = ?");
            params.add(type.name());
        }

        if (minPrice != null) {
            sql.append(" AND price >= ?");
            params.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append(" AND price <= ?");
            params.add(maxPrice);
        }

        List<Property> result = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(map(rs));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public List<Property> searchApproved(Connection conn,
                                         String address,
                                         String type) {

        List<Property> list = new ArrayList<>();

        String sql = """
        SELECT *
        FROM properties
        WHERE status = 'AVAILABLE'
        AND (? IS NULL OR address LIKE ?)
        AND (? IS NULL OR type = ?)
        ORDER BY created_at DESC
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, address);
            ps.setString(2, address == null ? null : "%" + address + "%");
            ps.setString(3, type);
            ps.setString(4, type);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Property p = map(rs);
                list.add(p);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public List<Property> searchCustomer(
            Connection conn,
            String address,
            String type,
            Integer minPrice,
            Integer maxPrice,
            String sort,
            int page,
            int size) {

        List<Property> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT *
        FROM properties
        WHERE status='AVAILABLE'
    """);

        if (address != null && !address.isEmpty()) {
            sql.append(" AND address LIKE ?");
        }

        if (type != null && !type.isEmpty()) {
            sql.append(" AND type = ?");
        }

        if (minPrice != null) {
            sql.append(" AND price >= ?");
        }

        if (maxPrice != null) {
            sql.append(" AND price <= ?");
        }

        if ("price_asc".equals(sort)) {
            sql.append(" ORDER BY price ASC");
        } else if ("price_desc".equals(sort)) {
            sql.append(" ORDER BY price DESC");
        } else {
            sql.append(" ORDER BY created_at DESC");
        }

        sql.append(" LIMIT ? OFFSET ?");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (address != null && !address.isEmpty()) {
                ps.setString(index++, "%" + address + "%");
            }

            if (type != null && !type.isEmpty()) {
                ps.setString(index++, type);
            }

            if (minPrice != null) {
                ps.setInt(index++, minPrice);
            }

            if (maxPrice != null) {
                ps.setInt(index++, maxPrice);
            }

            ps.setInt(index++, size);
            ps.setInt(index, (page - 1) * size);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Property p = map(rs);
                list.add(p);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public int countCustomer(
            Connection conn,
            String address,
            String type,
            Integer minPrice,
            Integer maxPrice) {

        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*)
        FROM properties
        WHERE status='AVAILABLE'
    """);

        if (address != null && !address.isEmpty()) {
            sql.append(" AND address LIKE ?");
        }

        if (type != null && !type.isEmpty()) {
            sql.append(" AND type = ?");
        }

        if (minPrice != null) {
            sql.append(" AND price >= ?");
        }

        if (maxPrice != null) {
            sql.append(" AND price <= ?");
        }

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (address != null && !address.isEmpty()) {
                ps.setString(index++, "%" + address + "%");
            }

            if (type != null && !type.isEmpty()) {
                ps.setString(index++, type);
            }

            if (minPrice != null) {
                ps.setInt(index++, minPrice);
            }

            if (maxPrice != null) {
                ps.setInt(index++, maxPrice);
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    @Override
    public List<Property> searchAvailable(Connection conn,
                                          PropertySearchDTO dto) {

        List<Property> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT *
        FROM properties
        WHERE status = 'AVAILABLE'
    """);

        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            sql.append(" AND title LIKE ?");
        }

        if (dto.getType() != null && !dto.getType().isEmpty()) {
            sql.append(" AND type = ?");
        }

        if (dto.getMinPrice() != null) {
            sql.append(" AND price >= ?");
        }

        if (dto.getMaxPrice() != null) {
            sql.append(" AND price <= ?");
        }

        sql.append(" ORDER BY created_at DESC LIMIT ? OFFSET ?");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
                ps.setString(index++, "%" + dto.getKeyword() + "%");
            }

            if (dto.getType() != null && !dto.getType().isEmpty()) {
                ps.setString(index++, dto.getType());
            }

            if (dto.getMinPrice() != null) {
                ps.setLong(index++, dto.getMinPrice());
            }

            if (dto.getMaxPrice() != null) {
                ps.setLong(index++, dto.getMaxPrice());
            }

            ps.setInt(index++, dto.getSize());
            ps.setInt(index, dto.getOffset());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Property> findSimilar(
            Connection conn,
            String type,
            String address,
            Long minPrice,
            Long maxPrice) {

        List<Property> list = new ArrayList<>();

        String sql = """
        SELECT *
        FROM properties
        WHERE type = ?
        AND address = ?
        AND price BETWEEN ? AND ?
        AND status = 'AVAILABLE'
        LIMIT 6
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, type);
            ps.setString(2, address);
            ps.setLong(3, minPrice);
            ps.setLong(4, maxPrice);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<PropertyCardDTO> searchAvailableCard(
            Connection conn,
            PropertySearchDTO dto) {

        List<PropertyCardDTO> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT 
            p.id,
            p.title,
            p.address,
            p.price,
            p.type,
            pi.image_url AS thumbnail
        FROM properties p
        LEFT JOIN property_images pi 
            ON pi.property_id = p.id
        WHERE p.status = 'AVAILABLE'
    """);

        if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
            sql.append(" AND p.title LIKE ?");
        }

        if (dto.getType() != null && !dto.getType().isEmpty()) {
            sql.append(" AND p.type = ?");
        }

        if (dto.getMinPrice() != null) {
            sql.append(" AND p.price >= ?");
        }

        if (dto.getMaxPrice() != null) {
            sql.append(" AND p.price <= ?");
        }

        sql.append(" ORDER BY p.created_at DESC LIMIT ? OFFSET ?");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (dto.getKeyword() != null && !dto.getKeyword().isEmpty()) {
                ps.setString(index++, "%" + dto.getKeyword() + "%");
            }

            if (dto.getType() != null && !dto.getType().isEmpty()) {
                ps.setString(index++, dto.getType());
            }

            if (dto.getMinPrice() != null) {
                ps.setLong(index++, dto.getMinPrice());
            }

            if (dto.getMaxPrice() != null) {
                ps.setLong(index++, dto.getMaxPrice());
            }

            ps.setInt(index++, dto.getSize());
            ps.setInt(index, dto.getOffset());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                PropertyCardDTO dtoCard = new PropertyCardDTO();

                dtoCard.setId(rs.getLong("id"));
                dtoCard.setTitle(rs.getString("title"));
                dtoCard.setAddress(rs.getString("address"));
                dtoCard.setPrice(rs.getBigDecimal("price"));
                dtoCard.setType(rs.getString("type"));
                dtoCard.setThumbnail(rs.getString("thumbnail"));

                list.add(dtoCard);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}