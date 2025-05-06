package com.costumeRental.userservice.dao;

import com.costumeRental.userservice.model.Staff;
import java.util.List;
import java.util.Optional;

public interface StaffDao {
    Staff save(Staff staff);
    Optional<Staff> findById(Long id);
    Staff findByUsername(String username);
    List<Staff> findAll();
    boolean existsById(Long id);
    void deleteById(Long id);
} 