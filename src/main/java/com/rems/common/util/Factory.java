package com.rems.common.util;

import com.rems.activitylog.dao.ActivityLogDAO;
import com.rems.activitylog.dao.impl.ActivityLogDAOImpl;
import com.rems.activitylog.service.ActivityLogService;
import com.rems.activitylog.service.impl.ActivityLogServiceImpl;
import com.rems.auth.dao.AuthAccountDAO;
import com.rems.auth.dao.UserOtpDAO;
import com.rems.auth.dao.impl.AuthAccountDAOImpl;
import com.rems.auth.dao.impl.UserOtpDAOImpl;
import com.rems.auth.service.AuthService;
import com.rems.auth.service.impl.AuthServiceImpl;
import com.rems.booking.dao.BookingDAO;
import com.rems.booking.dao.impl.BookingDAOImpl;
import com.rems.booking.service.BookingService;
import com.rems.booking.service.impl.BookingServiceImpl;
import com.rems.common.transaction.TransactionManager;
import com.rems.dashboard.service.DashboardService;
import com.rems.dashboard.service.impl.DashboardServiceImpl;
import com.rems.favorite.service.FavoriteService;
import com.rems.favorite.service.impl.FavoriteServiceImpl;
import com.rems.property.dao.PropertyDAO;
import com.rems.property.dao.PropertyImageDAO;
import com.rems.property.dao.impl.PropertyDAOImpl;
import com.rems.property.dao.impl.PropertyImageDAOImpl;
import com.rems.property.service.PropertyImageService;
import com.rems.property.service.PropertyService;
import com.rems.property.service.impl.PropertyImageServiceImpl;
import com.rems.property.service.impl.PropertyServiceImpl;
import com.rems.report.service.ReportService;
import com.rems.report.service.impl.ReportServiceImpl;
import com.rems.transaction.dao.TransactionDAO;
import com.rems.transaction.dao.impl.TransactionDAOImpl;
import com.rems.transaction.service.TransactionService;
import com.rems.transaction.service.impl.TransactionServiceImpl;
import com.rems.user.dao.UserDAO;
import com.rems.user.dao.impl.UserDAOImpl;
import com.rems.user.service.UserService;
import com.rems.user.service.impl.UserServiceImpl;

public final class Factory {

    private Factory() {
    }

    private static final TransactionManager txManager = new TransactionManager();

    // DAO (singleton)
    private static final BookingDAO bookingDAO = new BookingDAOImpl();
    private static final PropertyDAO propertyDAO = new PropertyDAOImpl();
    private static final UserDAO userDAO = new UserDAOImpl();
    private static final TransactionDAO transactionDAO = new TransactionDAOImpl();
    private static final ActivityLogDAO logDAO = new ActivityLogDAOImpl();
    private static final PropertyImageDAO propertyImageDAO = new PropertyImageDAOImpl();
    private static final AuthAccountDAO authAccountDAO = new AuthAccountDAOImpl();
    private static final UserOtpDAO userOtpDAO = new UserOtpDAOImpl();


    // SERVICE (singleton)
    private static final ActivityLogService activityLogService =
            new ActivityLogServiceImpl(txManager, logDAO);

    private static final PropertyImageService propertyImageService =
            new PropertyImageServiceImpl(propertyImageDAO, txManager);

    private static final TransactionService transactionService =
            new TransactionServiceImpl(
                    txManager,
                    transactionDAO,
                    bookingDAO,
                    propertyDAO,
                    userDAO,
                    activityLogService
            );

    private static final PropertyService propertyService =
            new PropertyServiceImpl(
                    propertyDAO,
                    txManager,
                    propertyImageDAO,
                    propertyImageService,
                    activityLogService);

    private static final BookingService bookingService =
            new BookingServiceImpl(
                    bookingDAO,
                    propertyDAO,
                    transactionService,
                    txManager
            );

    private static final AuthService authService =
            new AuthServiceImpl(
                    authAccountDAO,
                    userDAO,
                    userOtpDAO,
                    txManager
            );

    private static final UserService userService =
            new UserServiceImpl(
                    txManager,
                    userDAO,
                    authAccountDAO
            );

    private static final DashboardService dashboardService =
            new DashboardServiceImpl(txManager);

    private static final FavoriteService favoriteService =
            new FavoriteServiceImpl();

    private static final ReportService reportService =
            new ReportServiceImpl();

    // ===== PUBLIC GETTER =====

    public static BookingService getBookingService() {
        return bookingService;
    }

    public static TransactionService getTransactionService() {
        return transactionService;
    }

    public static PropertyService getPropertyService() {
        return propertyService;
    }

    public static PropertyImageService getPropertyImageService() {
        return propertyImageService;
    }

    public static ActivityLogService getActivityLogService() {
        return activityLogService;
    }

    public static AuthService getAuthService() {
        return authService;
    }

    public static UserService getUserService() {
        return userService;
    }

    public static DashboardService getDashboardService() {
        return dashboardService;
    }

    public static FavoriteService getFavoriteService() {
        return favoriteService;
    }

    public static ReportService getReportService() {
        return reportService;
    }
}
