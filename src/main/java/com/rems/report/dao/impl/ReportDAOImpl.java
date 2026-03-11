package com.rems.report.dao.impl;

import com.rems.common.util.DBUtil;
import com.rems.report.dao.ReportDAO;
import com.rems.report.model.dto.ReportDTO;
import com.rems.report.model.dto.RevenueReportDTO;
import com.rems.report.model.dto.TransactionStatDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public List<TransactionStatDTO> getTransactionStatistics() {

        String sql = """
            SELECT DATE_FORMAT(created_at,'%Y-%m') AS month,
                   COUNT(*) AS total
            FROM transactions
            WHERE status = 'COMPLETED'
            GROUP BY month
            ORDER BY month
        """;

        List<TransactionStatDTO> list = new ArrayList<>();

        try(Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {

                TransactionStatDTO dto = new TransactionStatDTO(
                        rs.getString("month"),
                        rs.getLong("total")
                );

                list.add(dto);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<RevenueReportDTO> getRevenueByMonth() {

        String sql = """
            SELECT DATE_FORMAT(created_at,'%Y-%m') AS month,
                   SUM(amount) AS revenue
            FROM transactions
            WHERE status = 'COMPLETED'
            GROUP BY month
            ORDER BY month
        """;

        List<RevenueReportDTO> list = new ArrayList<>();

        try(Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {

                RevenueReportDTO dto = new RevenueReportDTO(
                        rs.getString("month"),
                        rs.getBigDecimal("revenue")
                );

                list.add(dto);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<ReportDTO> getMonthlyReport() {

        String sql = """
        SELECT
            DATE_FORMAT(created_at,'%Y-%m') AS month,
            COUNT(*) AS transactions,
            SUM(amount) AS revenue
        FROM transactions
        WHERE status = 'COMPLETED'
        GROUP BY month
        ORDER BY month
    """;

        List<ReportDTO> list = new ArrayList<>();

        try(Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {

                ReportDTO dto = new ReportDTO(
                        rs.getString("month"),
                        rs.getBigDecimal("revenue"),
                        rs.getLong("transactions")
                );

                list.add(dto);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
