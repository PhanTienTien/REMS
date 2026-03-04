package com.rems.user.dao;

import com.rems.user.model.User;

public interface UserDAO {
    User findByEmail(String email);
    User findByAuthId(Long authId);
    void save(User user);
    void updateVerified(Long userId, boolean verified);
    void softDelete(Long userId);

}
