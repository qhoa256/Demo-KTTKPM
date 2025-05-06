package com.costumeRental.userservice.service;

import com.costumeRental.userservice.model.Staff;

import java.util.List;

public interface StaffService extends UserService {
    Staff createStaff(Staff staff);
    Staff getStaffById(Long id);
    List<Staff> getAllStaff();
    Staff updateStaff(Long id, Staff staff);
    void deleteStaff(Long id);
    Staff getStaffByUsername(String username);
    Staff getStaffByManagerCode(String managerCode);
    List<Staff> getStaffByPosition(String position);
} 