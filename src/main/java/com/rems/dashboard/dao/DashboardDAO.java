package com.rems.dashboard.dao;

import com.rems.dashboard.dto.RecentTransactionDTO;
import com.rems.dashboard.dto.RevenueChartDTO;
import com.rems.dashboard.dto.RevenuePointDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DashboardDAO {

    Map<String, Long> getPropertyStats(Connection conn) throws SQLException;

    Map<String, Long> getBookingStats(Connection conn) throws SQLException;

    Map<String, Long> getTransactionStats(Connection conn) throws SQLException;

    long countTotalProperties(Connection conn) throws SQLException;

    long countAvailableProperties(Connection conn) throws SQLException;

    long countTotalBookings(Connection conn) throws SQLException;

    long countCompletedTransactions(Connection conn) throws SQLException;

    BigDecimal sumTotalRevenue(Connection conn) throws SQLException;

    List<RecentTransactionDTO> findRecentTransactions(Connection conn, int limit) throws SQLException;

    List<RevenuePointDTO> getMonthlyRevenue(Connection conn) throws SQLException;

    long countDraftProperties(Connection conn) throws SQLException;

    long countPendingTransactions(Connection conn) throws SQLException;

    long countReservedTooLong(Connection conn) throws SQLException;

}
