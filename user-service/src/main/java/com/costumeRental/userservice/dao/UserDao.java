package com.costumeRental.userservice.dao;

import com.costumeRental.userservice.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    User save(User user);
    Optional<User> findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
    boolean existsById(Long id);
    void deleteById(Long id);
} 