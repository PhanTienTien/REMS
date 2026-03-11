package com.rems.user.service.impl;

import com.rems.auth.dao.AuthAccountDAO;
import com.rems.auth.model.AuthAccount;
import com.rems.common.constant.AccountStatus;
import com.rems.common.exception.BusinessException;
import com.rems.common.exception.ErrorCode;
import com.rems.common.transaction.TransactionManager;
import com.rems.common.util.PasswordUtil;
import com.rems.user.dao.UserDAO;
import com.rems.user.model.User;
import com.rems.user.model.dto.CreateUserByAdminDTO;
import com.rems.user.service.UserService;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final TransactionManager txManager;
    private final UserDAO userDAO;
    private final AuthAccountDAO authAccountDAO;

    public UserServiceImpl(TransactionManager txManager,
                           UserDAO userDAO, AuthAccountDAO authAccountDAO) {
        this.txManager = txManager;
        this.userDAO = userDAO;
        this.authAccountDAO = authAccountDAO;
    }

    @Override
    public Optional<User> findById(Long id) {
        return txManager.execute(conn ->
                userDAO.findById(conn, id));
    }

    @Override
    public User findByAuthId(Long authId) {

        return txManager.execute(conn ->
                userDAO.findByAuthId(conn, authId)
                        .orElseThrow(() ->
                                new BusinessException(ErrorCode.USER_NOT_FOUND))
        );
    }

    @Override
    public List<User> getAllUsers() {

        return txManager.execute(conn ->
                userDAO.findAll(conn)
        );
    }

    @Override
    public Long createUserByAdmin(CreateUserByAdminDTO dto) {

        return txManager.execute(conn -> {

            AuthAccount account = AuthAccount.local(
                    dto.getEmail(),
                    PasswordUtil.hash(dto.getPassword())
            );

            account.setStatus(AccountStatus.ACTIVE);

            Long authId = authAccountDAO.save(conn, account);

            User user = new User();
            user.setAuthId(authId);
            user.setFullName(dto.getFullName());
            user.setEmail(dto.getEmail());
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setRole(dto.getRole());
            user.setVerified(true);
            user.setDeleted(false);

            userDAO.save(conn, user);

            return authId;
        });
    }

    @Override
    public void updateUser(User user) {

        txManager.executeWithoutResult(conn -> {
            userDAO.update(conn, user);
        });
    }

    @Override
    public void deleteUser(Long id) {

        txManager.executeWithoutResult(conn -> {
            userDAO.softDelete(conn, id);
        });
    }

    @Override
    public List<User> searchUsers(String keyword,
                                  String role,
                                  Boolean active,
                                  int page,
                                  int size) {

        return txManager.execute(conn ->
                userDAO.search(conn, keyword, role, active, page, size)
        );
    }

    @Override
    public long countUsers(String keyword,
                           String role) {

        return txManager.execute(conn ->
                userDAO.countUsers(conn, keyword, role)
        );
    }
}