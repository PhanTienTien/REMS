package com.rems.activitylog.dao;

import com.rems.activitylog.model.ActivityLog;

import java.util.List;

public interface ActivityLogDAO {

    void save(ActivityLog log);

    List<ActivityLog> findAll(int limit, int offset, String user, String action, String fromDate, String toDate);

}
