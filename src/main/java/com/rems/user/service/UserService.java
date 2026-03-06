package com.rems.user.service;

import com.rems.common.constant.Role;
import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;
import com.rems.common.util.DBUtil;
import com.rems.user.dao.impl.UserDAOImpl;
import com.rems.user.model.User;

import java.sql.Connection;
import java.util.Optional;

public interface UserService {
    User findByAuthId(Long authId);
}