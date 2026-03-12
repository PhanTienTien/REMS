package com.rems.activitylog.service;

import com.rems.activitylog.model.ActivityLog;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ActivityLogService {

    void log(HttpServletRequest request,
             Long userId,
             String action,
             String entityType,
             Long entityId,
             String description);

    List<ActivityLog> getLogs(int page,
                              String user,
                              String action,
                              String fromDate,
                              String toDate);

    void logView(Long userId, Long propertyId);

}
