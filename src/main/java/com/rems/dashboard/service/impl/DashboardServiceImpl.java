package com.rems.dashboard.service.impl;

import com.rems.common.transaction.TransactionManager;
import com.rems.common.util.Factory;
import com.rems.dashboard.dao.DashboardDAO;
import com.rems.dashboard.dao.impl.DashboardDAOImpl;
import com.rems.dashboard.dto.*;
import com.rems.dashboard.service.DashboardService;
import com.rems.property.dto.TopViewedPropertyDTO;
import com.rems.property.service.PropertyService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DashboardServiceImpl implements DashboardService {

    private final DashboardDAO dashboardDAO = new DashboardDAOImpl();
    private final TransactionManager txManager;
    private final PropertyService propertyService = Factory.getPropertyService();

    public DashboardServiceImpl(TransactionManager txManager) {
        this.txManager = txManager;
    }

    @Override
    public AdminDashboardDTO getAdminDashboard() {

        return txManager.execute(conn -> {

            try {

                long totalProperties = dashboardDAO.countTotalProperties(conn);
                long availableProperties = dashboardDAO.countAvailableProperties(conn);
                long totalBookings = dashboardDAO.countTotalBookings(conn);
                long completedTransactions = dashboardDAO.countCompletedTransactions(conn);
                BigDecimal totalRevenue = dashboardDAO.sumTotalRevenue(conn);

                List<DashboardCardDTO> cards = buildCards(
                        totalProperties,
                        availableProperties,
                        totalBookings,
                        completedTransactions,
                        totalRevenue
                );

                List<RecentTransactionDTO> recentTransactions =
                        dashboardDAO.findRecentTransactions(conn, 10);

                List<RevenuePointDTO> revenuePoints =
                        dashboardDAO.getMonthlyRevenue(conn);

                Map<String, Long> propertyStats =
                        dashboardDAO.getPropertyStats(conn);
                Map<String, Long> transactionStats =
                        dashboardDAO.getTransactionStats(conn);
                long draftProperties = dashboardDAO.countDraftProperties(conn);
                long pendingTransactions = dashboardDAO.countPendingTransactions(conn);
                long reservedTooLong = dashboardDAO.countReservedTooLong(conn);

                RevenueChartDTO chart = new RevenueChartDTO();
                chart.setPoints(revenuePoints);

                AdminDashboardDTO dto = new AdminDashboardDTO();
                dto.setCards(cards);
                dto.setRecentTransactions(recentTransactions);
                dto.setRevenueChart(chart);
                dto.setPropertyStats(propertyStats);
                dto.setTransactionStats(transactionStats);
                dto.setDraftProperties(draftProperties);
                dto.setPendingTransactions(pendingTransactions);
                dto.setReservedTooLong(reservedTooLong);

                // Get top 20 viewed properties
                List<TopViewedPropertyDTO> topViewed = propertyService.getTopViewedProperties(20);
                dto.setTopViewedProperties(topViewed);

                return dto;

            } catch (SQLException e) {
                throw new RuntimeException("Failed to load dashboard data", e);
            }
        });
    }

    @Override
    public StaffDashboardDTO getStaffDashboard(long userId) {

        return txManager.execute(conn -> {

            try {

                long myDraft = dashboardDAO.countMyDraftProperties(conn, userId);
                long myActive = dashboardDAO.countMyActiveProperties(conn, userId);
                long myTransactions = dashboardDAO.countMyTransactions(conn, userId);

                StaffDashboardDTO dto = new StaffDashboardDTO();
                dto.setMyDraftProperties(myDraft);
                dto.setMyActiveProperties(myActive);
                dto.setMyTransactions(myTransactions);

                return dto;

            } catch (SQLException e) {
                throw new RuntimeException("Failed to load staff dashboard", e);
            }
        });
    }

    private List<DashboardCardDTO> buildCards(
            long totalProperties,
            long availableProperties,
            long totalBookings,
            long completedTransactions,
            BigDecimal totalRevenue
    ) {

        List<DashboardCardDTO> cards = new ArrayList<>();

        cards.add(new DashboardCardDTO("Tổng bất động sản", totalProperties));
        cards.add(new DashboardCardDTO("Bất động sản đang mở", availableProperties));
        cards.add(new DashboardCardDTO("Tổng lượt đặt lịch", totalBookings));
        cards.add(new DashboardCardDTO("Giao dịch hoàn tất", completedTransactions));
        cards.add(new DashboardCardDTO("Tổng doanh thu", totalRevenue.longValue()));

        return cards;
    }
}
