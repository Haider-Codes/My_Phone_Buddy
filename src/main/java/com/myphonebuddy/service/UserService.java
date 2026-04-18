package com.myphonebuddy.service;

import com.myphonebuddy.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User saveUser(User user);

    Optional<User> findUserByEmail(String email);

    User updateUser(User user);

    void deleteUser(User user);

    boolean isUserExistByEmail(String email);

    List<User> findAllUsers();

    Optional<User> findUserByToken(String token);
}
