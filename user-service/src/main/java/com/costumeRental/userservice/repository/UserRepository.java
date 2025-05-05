package com.costumeRental.userservice.repository;

import com.costumeRental.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
} 