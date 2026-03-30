package com.rems.activitylog.service.impl;

import com.rems.activitylog.dao.ActivityLogDAO;
import com.rems.activitylog.model.ActivityLog;
import com.rems.activitylog.service.ActivityLogService;
import com.rems.common.constant.ActivityLogAction;
import com.rems.common.transaction.TransactionManager;
import com.rems.property.dto.PropertyCardDTO;

import java.sql.Connection;
import java.util.List;

public class ActivityLogServiceImpl implements ActivityLogService {

    private final TransactionManager txManager;
    private final ActivityLogDAO logDAO;

    public ActivityLogServiceImpl(TransactionManager txManager,
                                  ActivityLogDAO logDAO) {
        this.txManager = txManager;
        this.logDAO = logDAO;
    }

    public ActivityLogServiceImpl(ActivityLogDAO logDAO,
                                  TransactionManager txManager) {
        this(txManager, logDAO);
    }

    public void log(Connection conn,
                    Long userId,
                    String action,
                    String entityType,
                    Long entityId,
                    String description,
                    String ipAddress) {

        ActivityLog log = new ActivityLog();

        log.setUserId(userId);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDescription(description);
        log.setIpAddress(ipAddress);

        logDAO.insert(conn, log);
    }

    @Override
    public void log(Connection conn,
                    Long userId,
                    ActivityLogAction action,
                    Long entityId,
                    String ipAddress,
                    Object... descriptionArgs) {
        log(
                conn,
                userId,
                action.getAction(),
                action.getEntityType(),
                entityId,
                action.buildDescription(descriptionArgs),
                ipAddress
        );
    }

    @Override
    public List<ActivityLog> getLogs(int page,
                                     String user,
                                     String action,
                                     String fromDate,
                                     String toDate) {

        int limit = 20;
        int offset = (page - 1) * limit;

        return logDAO.findAll(limit, offset, user, action, fromDate, toDate);
    }

    @Override
    public int countLogs(String user,
                         String action,
                         String fromDate,
                         String toDate) {
        return logDAO.count(user, action, fromDate, toDate);
    }

    @Override
    public void logView(Long userId, Long propertyId) {

        txManager.execute(conn -> {

            logDAO.insertView(conn, userId, propertyId);

            return null;
        });

    }

    @Override
    public List<PropertyCardDTO> getMostViewedProperties(int limit) {
        return logDAO.findMostViewedProperties(limit);
    }
}
