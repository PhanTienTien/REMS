package com.rems.user.service.impl;

import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;
import com.rems.common.transaction.TransactionManager;
import com.rems.user.dao.UserDAO;
import com.rems.user.model.User;
import com.rems.user.service.UserService;

public class UserServiceImpl implements UserService {

    private final TransactionManager txManager;
    private final UserDAO userDAO;

    public UserServiceImpl(TransactionManager txManager,
                           UserDAO userDAO) {
        this.txManager = txManager;
        this.userDAO = userDAO;
    }

    @Override
    public User findByAuthId(Long authId) {

        return txManager.execute(conn ->
                userDAO.findByAuthId(conn, authId)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.USER_NOT_FOUND))
        );
    }
}