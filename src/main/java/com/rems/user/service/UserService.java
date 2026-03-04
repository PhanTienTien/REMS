package com.rems.user.service;

import com.rems.common.constant.Role;
import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;
import com.rems.common.util.DBUtil;
import com.rems.user.dao.impl.UserDAOImpl;
import com.rems.user.model.User;

import java.sql.Connection;

public class UserService {

//    public void createProfile(Long authId,
//                              String fullName,
//                              String email,
//                              Role role) {
//
//        Connection conn = null;
//
//        try {
//            conn = DBUtil.getConnection();
//            conn.setAutoCommit(false);
//
//            UserDAOImpl userDAO = new UserDAOImpl(conn);
//
//            if (userDAO.findByEmail(email) != null)
//                throw new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS);
//
////            User user = User.create(authId, fullName, email, role);
////            userDAO.save(user);
//
//            conn.commit();
//
//        } catch (Exception e) {
//            try { if (conn != null) conn.rollback(); } catch (Exception ignored) {}
//            throw new RuntimeException(e);
//        } finally {
//            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
//        }
//    }
//
//    public User getByAuthId(Long authId) throws Exception {
//
//        try (Connection conn = DBUtil.getConnection()) {
//
//            UserDAOImpl userDAO = new UserDAOImpl(conn);
//
//            User user = userDAO.findByAuthId(authId);
//
//            if (user == null)
//                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
//
//            return user;
//        }
//    }
}