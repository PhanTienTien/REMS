package com.rems.activitylog.service;

import com.rems.activitylog.model.ActivityLog;
import com.rems.common.constant.ActivityLogAction;
import com.rems.property.dto.PropertyCardDTO;

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

    void log(Connection conn,
             Long userId,
             ActivityLogAction action,
             Long entityId,
             String ipAddress,
             Object... descriptionArgs);

    List<ActivityLog> getLogs(int page,
                              String user,
                              String action,
                              String fromDate,
                              String toDate);

    int countLogs(String user,
                  String action,
                  String fromDate,
                  String toDate);

    void logView(Long userId, Long propertyId);

    List<PropertyCardDTO> getMostViewedProperties(int limit);

}
