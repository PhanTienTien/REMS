package com.rems.property.job;

import com.rems.common.transaction.TransactionManager;
import com.rems.common.util.Factory;
import com.rems.property.dao.PropertyDAO;
import com.rems.property.service.PropertyService;

import java.sql.Connection;

public class PropertyReservationExpirationJob {

    private final TransactionManager txManager = Factory.getTxManager();
    private final PropertyService propertyService = Factory.getPropertyService();

    public void expireLongReservations() {

        txManager.executeWithoutResult(conn -> {
            
            // Find properties that have been RESERVED for more than 48 hours
            // and change them back to AVAILABLE
            String sql = """
                UPDATE properties 
                SET status = 'AVAILABLE', 
                    updated_at = NOW()
                WHERE status = 'RESERVED' 
                AND updated_at < DATE_SUB(NOW(), INTERVAL 48 HOUR)
                """;

            try (var ps = conn.prepareStatement(sql)) {
                int updated = ps.executeUpdate();
                System.out.println("Expired " + updated + " long reservations");
            } catch (Exception e) {
                throw new RuntimeException("Failed to expire long reservations", e);
            }
        });
    }
}
