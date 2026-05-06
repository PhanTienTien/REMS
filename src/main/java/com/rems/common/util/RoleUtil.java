package com.rems.common.util;

import com.rems.user.model.User;
import com.rems.common.constant.Role;

public class RoleUtil {
    
    public static boolean canManageBookings(User user) {
        return user != null && (user.getRole() == Role.ADMIN || user.getRole() == Role.STAFF);
    }
    
    public static boolean canManageTransactions(User user) {
        return user != null && (user.getRole() == Role.ADMIN || user.getRole() == Role.STAFF);
    }
    
    public static boolean isAdmin(User user) {
        return user != null && user.getRole() == Role.ADMIN;
    }
    
    public static boolean isStaff(User user) {
        return user != null && user.getRole() == Role.STAFF;
    }
    
    public static boolean isCustomer(User user) {
        return user != null && user.getRole() == Role.CUSTOMER;
    }
}
