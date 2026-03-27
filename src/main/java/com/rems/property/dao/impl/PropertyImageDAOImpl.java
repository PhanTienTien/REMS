package com.rems.property.dao.impl;

import com.rems.property.dao.PropertyImageDAO;
import com.rems.property.model.PropertyImage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyImageDAOImpl implements PropertyImageDAO {

    @Override
    public List<PropertyImage> findByPropertyId(Connection conn, Long propertyId) {

        List<PropertyImage> list = new ArrayList<>();

        String sql = """
            SELECT id, property_id, image_url
            FROM property_images
            WHERE property_id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, propertyId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                PropertyImage img = new PropertyImage();
                img.setId(rs.getLong("id"));
                img.setPropertyId(rs.getLong("property_id"));
                img.setImageUrl(rs.getString("image_url"));

                list.add(img);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public void insert(Connection conn,
                       Long propertyId,
                       String imageUrl) {

        String sql = """
        INSERT INTO property_images(property_id,image_url)
        VALUES (?,?)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, propertyId);
            ps.setString(2, imageUrl);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getThumbnail(Connection conn, Long propertyId) {

        String sql = """
        SELECT image_url
        FROM property_images
        WHERE property_id = ?
        ORDER BY is_thumbnail DESC
        LIMIT 1
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, propertyId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("image_url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void insertMultiple(Connection conn,
                               Long propertyId,
                               List<String> imageUrls) {

        String sql = """
        INSERT INTO property_images
        (property_id, image_url, is_thumbnail, sort_order)
        VALUES (?,?,?,?)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            int order = 0;

            for (String url : imageUrls) {

                ps.setLong(1, propertyId);
                ps.setString(2, url);
                ps.setBoolean(3, order == 0);
                ps.setInt(4, order++);

                ps.addBatch();
            }

            ps.executeBatch();
            System.out.println("Insert images size: " + imageUrls.size());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Connection conn, Long id) {

        String sql = "DELETE FROM property_images WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetThumbnail(Connection conn, Long propertyId) {

        String sql = "UPDATE property_images SET is_thumbnail = false WHERE property_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, propertyId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}