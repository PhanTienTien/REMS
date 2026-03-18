package com.rems.activitylog.service;

import com.rems.activitylog.model.ActivityLog;

import java.sql.Connection;
import java.util.List;

public interface ActivityLogService {

    void log(Connection conn,
             Long userId,
             String action,
             String entityType,
             Long entityId,
             String description,
             String ipAddress);

    List<ActivityLog> getLogs(int page,
                              String user,
                              String action,
                              String fromDate,
                              String toDate);

    void logView(Long userId, Long propertyId);

}
