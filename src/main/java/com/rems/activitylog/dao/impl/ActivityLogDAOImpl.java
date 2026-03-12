package com.rems.activitylog.dao.impl;

import com.rems.activitylog.dao.ActivityLogDAO;
import com.rems.activitylog.model.ActivityLog;
import com.rems.common.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDAOImpl implements ActivityLogDAO {

    @Override
    public void save(ActivityLog log) {

        String sql = """
            INSERT INTO activity_logs
            (user_id, action, entity_type, entity_id, description, ip_address)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, log.getUserId());
            ps.setString(2, log.getAction());
            ps.setString(3, log.getEntityType());
            ps.setLong(4, log.getEntityId());
            ps.setString(5, log.getDescription());
            ps.setString(6, log.getIpAddress());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ActivityLog> findAll(int limit, int offset,
                                     String user,
                                     String action,
                                     String fromDate,
                                     String toDate) {

        List<ActivityLog> logs = new ArrayList<>();

        String sql = """
        SELECT
            l.id,
            u.full_name,
            l.action,
            l.entity_type,
            l.entity_id,
            l.description,
            l.ip_address,
            l.created_at
        FROM activity_logs l
        JOIN users u ON u.id = l.user_id
        WHERE 1=1
        """;

        if (user != null && !user.isEmpty()) {
            sql += " AND u.full_name LIKE ?";
        }

        if (action != null && !action.isEmpty()) {
            sql += " AND l.action = ?";
        }

        if (fromDate != null && !fromDate.isEmpty()) {
            sql += " AND l.created_at >= ?";
        }

        if (toDate != null && !toDate.isEmpty()) {
            sql += " AND l.created_at <= ?";
        }

        sql += " ORDER BY l.created_at DESC LIMIT ? OFFSET ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;

            if (user != null && !user.isEmpty()) {
                ps.setString(index++, "%" + user + "%");
            }

            if (action != null && !action.isEmpty()) {
                ps.setString(index++, action);
            }

            if (fromDate != null && !fromDate.isEmpty()) {
                ps.setString(index++, fromDate);
            }

            if (toDate != null && !toDate.isEmpty()) {
                ps.setString(index++, toDate);
            }

            ps.setInt(index++, limit);
            ps.setInt(index, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ActivityLog log = new ActivityLog();

                log.setId(rs.getLong("id"));
                log.setFullName(rs.getString("full_name"));
                log.setAction(rs.getString("action"));
                log.setEntityType(rs.getString("entity_type"));
                log.setEntityId(rs.getLong("entity_id"));
                log.setDescription(rs.getString("description"));
                log.setIpAddress(rs.getString("ip_address"));
                log.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                logs.add(log);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return logs;
    }

    @Override
    public void insertView(Connection conn,
                           Long userId,
                           Long propertyId) {

        String sql = """
        INSERT INTO activity_logs
        (user_id, property_id, action, created_at)
        VALUES (?, ?, 'VIEW_PROPERTY', NOW())
    """;

        try(PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setLong(1, userId);
            ps.setLong(2, propertyId);

            ps.executeUpdate();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}