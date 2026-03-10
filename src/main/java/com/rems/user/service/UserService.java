package com.rems.user.service;

import com.rems.user.model.dto.CreateUserByAdminDTO;
import com.rems.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long id);

    User findByAuthId(Long authId);

    List<User> getAllUsers();

    void updateUser(User user);

    void deleteUser(Long id);

    List<User> searchUsers(String keyword,
                           String role,
                           Boolean active,
                           int page,
                           int size);

    long countUsers(String keyword, String role);

    Long createUserByAdmin(CreateUserByAdminDTO dto);
}