package com.rems;

import com.rems.common.util.DBUtil;
import com.rems.dashboard.dao.DashboardDAO;
import com.rems.dashboard.dao.impl.DashboardDAOImpl;

import java.sql.Connection;

public class DashboardDAOTest {

    public static void main(String[] args) throws Exception {

        Connection conn = DBUtil.getConnection();

        DashboardDAO dao = new DashboardDAOImpl();

        System.out.println("Property Stats: " + dao.getPropertyStats(conn));
        System.out.println("Booking Stats: " + dao.getBookingStats(conn));
        System.out.println("Transaction Stats: " + dao.getTransactionStats(conn));
    }
}
