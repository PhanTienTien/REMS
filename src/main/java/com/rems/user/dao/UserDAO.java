package com.rems.user.dao;

import com.rems.user.model.User;
import com.rems.user.model.dto.CreateUserByAdminDTO;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface UserDAO {

    List<User> findAll(Connection conn);

    Optional<User> findById(Connection conn, Long id);

    Optional<User> findByEmail(Connection conn, String email);

    Optional<User> findByAuthId(Connection conn, Long authId);

    void save(Connection conn, User user);

    void update(Connection conn, User user);

    void updateVerified(Connection conn, Long userId, boolean verified);

    void softDelete(Connection conn, Long userId);

    void restore(Connection conn, Long userId);

    long countUsers(Connection conn, String keyword, String role);

    List<User> search(Connection conn,
                      String keyword,
                      String role,
                      Boolean active,
                      int page,
                      int size);

    void saveCreateByAdmin(Connection conn, CreateUserByAdminDTO createUserByAdminDTO);
}
