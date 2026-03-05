package com.rems.user.dao;

import com.rems.user.model.User;

import java.sql.Connection;
import java.util.Optional;

public interface UserDAO {

    Optional<User> findById(Connection conn, Long id);

    Optional<User> findByEmail(Connection conn, String email);

    Optional<User> findByAuthId(Connection conn, Long authId);

    void save(Connection conn, User user);

    void updateVerified(Connection conn, Long userId, boolean verified);

    void softDelete(Connection conn, Long userId);
}
