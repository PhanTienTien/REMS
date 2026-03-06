package com.rems.dashboard.service;

import com.rems.dashboard.dto.AdminDashboardDTO;
import com.rems.dashboard.dto.StaffDashboardDTO;

public interface DashboardService {

    AdminDashboardDTO getAdminDashboard();

    StaffDashboardDTO getStaffDashboard(long userId);

}
