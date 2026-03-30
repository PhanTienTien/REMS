package com.rems.activitylog.dao;

import com.rems.activitylog.model.ActivityLog;
import com.rems.property.dto.PropertyCardDTO;

import java.sql.Connection;
import java.util.List;

public interface ActivityLogDAO {

    void save(ActivityLog log);

    List<ActivityLog> findAll(int limit, int offset, String user, String action, String fromDate, String toDate);

    int count(String user, String action, String fromDate, String toDate);

    void insertView(Connection conn,
                    Long userId,
                    Long propertyId);

    void insert(Connection conn, ActivityLog log);

    List<PropertyCardDTO> findMostViewedProperties(int limit);

}
