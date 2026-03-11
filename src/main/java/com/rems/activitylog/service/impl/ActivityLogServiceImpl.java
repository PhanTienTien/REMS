package com.rems.activitylog.service.impl;

import com.rems.activitylog.dao.ActivityLogDAO;
import com.rems.activitylog.model.ActivityLog;
import com.rems.activitylog.service.ActivityLogService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class ActivityLogServiceImpl implements ActivityLogService {

    private final ActivityLogDAO activityLogDao;

    public ActivityLogServiceImpl(ActivityLogDAO activityLogDao) {
        this.activityLogDao = activityLogDao;
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

        activityLogDao.save(log);
    }

    @Override
    public List<ActivityLog> getLogs(int page,
                                     String user,
                                     String action,
                                     String fromDate,
                                     String toDate) {

        int limit = 20;
        int offset = (page - 1) * limit;

        return activityLogDao.findAll(limit, offset, user, action, fromDate, toDate);
    }
}
