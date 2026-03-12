package com.rems.activitylog.service.impl;

import com.rems.activitylog.dao.ActivityLogDAO;
import com.rems.activitylog.model.ActivityLog;
import com.rems.activitylog.service.ActivityLogService;
import com.rems.common.transaction.TransactionManager;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogDAO activityLogDAO;
    private final TransactionManager txManager;

    public ActivityLogServiceImpl(ActivityLogDAO activityLogDAO, TransactionManager txManager) {
        this.activityLogDAO = activityLogDAO;
        this.txManager = txManager;
    }

    @Override
    public void log(HttpServletRequest request,
                    Long userId,
                    String action,
                    String entityType,
                    Long entityId,
                    String description) {

        ActivityLog log = new ActivityLog();

        log.setUserId(userId);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDescription(description);

        log.setIpAddress(request.getRemoteAddr());

        activityLogDAO.save(log);
    }

    @Override
    public List<ActivityLog> getLogs(int page,
                                     String user,
                                     String action,
                                     String fromDate,
                                     String toDate) {

        int limit = 20;
        int offset = (page - 1) * limit;

        return activityLogDAO.findAll(limit, offset, user, action, fromDate, toDate);
    }

    @Override
    public void logView(Long userId, Long propertyId) {

        txManager.execute(conn -> {

            activityLogDAO.insertView(conn, userId, propertyId);

            return null;
        });

    }
}
