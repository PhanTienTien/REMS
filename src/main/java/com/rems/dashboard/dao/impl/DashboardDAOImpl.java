package com.rems.dashboard.dao.impl;

import com.rems.dashboard.dao.DashboardDAO;
import com.rems.dashboard.dto.RecentTransactionDTO;
import com.rems.dashboard.dto.RevenueChartDTO;
import com.rems.dashboard.dto.RevenuePointDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardDAOImpl implements DashboardDAO {

    @Override
    public long countTotalProperties(Connection conn) throws SQLException {

        String sql = "SELECT COUNT(*) FROM properties";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        }

        return 0;
    }

    @Override
    public Map<String, Long> getPropertyStats(Connection conn) throws SQLException {

        String sql = """
                SELECT status, COUNT(*) AS total
                FROM properties
                GROUP BY status
                """;

        Map<String, Long> result = new HashMap<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.put(
                        rs.getString("status"),
                        rs.getLong("total")
                );
            }
        }

        return result;
    }

    @Override
    public Map<String, Long> getBookingStats(Connection conn) throws SQLException {

        String sql = """
                SELECT status, COUNT(*) AS total
                FROM bookings
                GROUP BY status
                """;

        Map<String, Long> result = new HashMap<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.put(
                        rs.getString("status"),
                        rs.getLong("total")
                );
            }
        }

        return result;
    }

    @Override
    public Map<String, Long> getTransactionStats(Connection conn) throws SQLException {

        String sql = """
        SELECT status, COUNT(*) AS total
        FROM transactions
        GROUP BY status
    """;

        Map<String, Long> stats = new HashMap<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                stats.put(
                        rs.getString("status"),
                        rs.getLong("total")
                );

            }
        }

        return stats;
    }

    @Override
    public long countAvailableProperties(Connection conn) throws SQLException {

        String sql = """
        SELECT COUNT(*)
        FROM properties
        WHERE status = 'AVAILABLE'
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        }

        return 0;
    }

    @Override
    public long countTotalBookings(Connection conn) throws SQLException {

        String sql = "SELECT COUNT(*) FROM bookings";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        }

        return 0;
    }

    @Override
    public long countCompletedTransactions(Connection conn) throws SQLException {

        String sql = """
        SELECT COUNT(*)
        FROM transactions
        WHERE status = 'COMPLETED'
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }
        }

        return 0;
    }

    @Override
    public BigDecimal sumTotalRevenue(Connection conn) throws SQLException {

        String sql = """
        SELECT SUM(amount)
        FROM transactions
        WHERE status = 'COMPLETED'
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                BigDecimal result = rs.getBigDecimal(1);

                return result != null ? result : BigDecimal.ZERO;
            }
        }

        return BigDecimal.ZERO;
    }

    @Override
    public List<RecentTransactionDTO> findRecentTransactions(Connection conn, int limit)
            throws SQLException {

        String sql = """
        SELECT
            t.id,
            t.property_title_snapshot,
            t.customer_name_snapshot,
            t.amount,
            t.status,
            t.created_at,
            t.type,
            u.full_name AS staff_name
        FROM transactions t
        LEFT JOIN users u ON t.processed_by = u.id
        ORDER BY t.created_at DESC
        LIMIT ?
        """;

        List<RecentTransactionDTO> list = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    RecentTransactionDTO dto = new RecentTransactionDTO();

                    dto.setTransactionId(rs.getLong("id"));
                    dto.setPropertyTitle(rs.getString("property_title_snapshot"));
                    dto.setCustomerName(rs.getString("customer_name_snapshot"));
                    dto.setAmount(rs.getBigDecimal("amount"));
                    dto.setStatus(rs.getString("status"));
                    dto.setDate(rs.getTimestamp("created_at").toLocalDateTime());
                    dto.setType(rs.getString("type"));
                    dto.setProcessedBy(rs.getString("staff_name"));

                    list.add(dto);
                }
            }
        }

        return list;
    }

    @Override
    public List<RevenuePointDTO> getMonthlyRevenue(Connection conn) throws SQLException {

        String sql = """
        SELECT
            MONTH(created_at) AS month,
            SUM(amount) AS revenue
        FROM transactions
        WHERE status = 'COMPLETED'
        AND created_at >= DATE_FORMAT(NOW(), '%Y-01-01')
        GROUP BY MONTH(created_at)
        ORDER BY month
        """;

        List<RevenuePointDTO> points = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                points.add(
                        new RevenuePointDTO(
                                rs.getString("month"),
                                rs.getBigDecimal("revenue")
                        )
                );
            }
        }

        return points;
    }

    @Override
    public long countDraftProperties(Connection conn) throws SQLException {

        String sql = """
        SELECT COUNT(*)
        FROM properties
        WHERE status = 'DRAFT'
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

            return 0;
        }
    }

    @Override
    public long countPendingTransactions(Connection conn) throws SQLException {

        String sql = """
        SELECT COUNT(*)
        FROM transactions
        WHERE status = 'PENDING'
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

            return 0;
        }
    }

    @Override
    public long countReservedTooLong(Connection conn) throws SQLException {

        String sql = """
        SELECT COUNT(*)
        FROM properties
        WHERE status = 'RESERVED'
        AND reserved_at < DATE_SUB(NOW(), INTERVAL 48 HOUR)
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

            return 0;
        }
    }

    @Override
    public long countMyDraftProperties(Connection conn, long userId) throws SQLException {

        String sql = """
        SELECT COUNT(*)
        FROM properties
        WHERE created_by = ?
        AND status = 'DRAFT'
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

            return 0;
        }
    }

    @Override
    public long countMyActiveProperties(Connection conn, long userId) throws SQLException {

        String sql = """
        SELECT COUNT(*)
        FROM properties
        WHERE created_by = ?
        AND status IN ('AVAILABLE','RESERVED')
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

            return 0;
        }
    }

    @Override
    public long countMyTransactions(Connection conn, long userId) throws SQLException {

        String sql = """
        SELECT COUNT(*)
        FROM transactions
        WHERE processed_by = ?
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1);
            }

            return 0;
        }
    }
}
