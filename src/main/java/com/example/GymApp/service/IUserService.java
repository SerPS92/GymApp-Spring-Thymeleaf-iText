package com.example.GymApp.service;

import com.example.GymApp.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> findAll();
    Optional<User> findById(Integer id);
    User save(User user);
    void delete(Integer id);
    void update(User user);
}
