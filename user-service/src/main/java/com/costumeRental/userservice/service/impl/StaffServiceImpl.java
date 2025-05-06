package com.costumeRental.userservice.service.impl;

import com.costumeRental.userservice.dao.StaffDao;
import com.costumeRental.userservice.model.Address;
import com.costumeRental.userservice.model.FullName;
import com.costumeRental.userservice.model.Staff;
import com.costumeRental.userservice.model.User;
import com.costumeRental.userservice.service.StaffService;
import com.costumeRental.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffDao staffDao;
    private final UserService userService;

    @Override
    public Staff createStaff(Staff staff) {
        return staffDao.save(staff);
    }

    @Override
    public Staff getStaffById(Long id) {
        return staffDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + id));
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffDao.findAll();
    }

    @Override
    public Staff updateStaff(Long id, Staff staffDetails) {
        Staff existingStaff = staffDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + id));
        
        existingStaff.setUsername(staffDetails.getUsername());
        existingStaff.setPassword(staffDetails.getPassword());
        existingStaff.setPosition(staffDetails.getPosition());
        
        if (staffDetails.getAddress() != null) {
            Address address = existingStaff.getAddress();
            if (address == null) {
                address = new Address();
                address.setUser(existingStaff);
                existingStaff.setAddress(address);
            }
            address.setCity(staffDetails.getAddress().getCity());
            address.setStreet(staffDetails.getAddress().getStreet());
        }
        
        if (staffDetails.getFullName() != null) {
            FullName fullName = existingStaff.getFullName();
            if (fullName == null) {
                fullName = new FullName();
                fullName.setUser(existingStaff);
                existingStaff.setFullName(fullName);
            }
            fullName.setFirstName(staffDetails.getFullName().getFirstName());
            fullName.setLastName(staffDetails.getFullName().getLastName());
        }
        
        return staffDao.save(existingStaff);
    }

    @Override
    public void deleteStaff(Long id) {
        if (!staffDao.existsById(id)) {
            throw new EntityNotFoundException("Staff not found with id: " + id);
        }
        staffDao.deleteById(id);
    }

    @Override
    public Staff getStaffByUsername(String username) {
        Staff staff = staffDao.findByUsername(username);
        if (staff == null) {
            throw new EntityNotFoundException("Staff not found with username: " + username);
        }
        return staff;
    }

    // DAO does not support these methods anymore
    @Override
    public Staff getStaffByManagerCode(String managerCode) {
        throw new UnsupportedOperationException("Operation not supported with DAO implementation");
    }

    @Override
    public List<Staff> getStaffByPosition(String position) {
        throw new UnsupportedOperationException("Operation not supported with DAO implementation");
    }

    // UserService methods delegation
    @Override
    public User createUser(User user) {
        return userService.createUser(user);
    }

    @Override
    public User getUserById(Long id) {
        return userService.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public User updateUser(Long id, User user) {
        return userService.updateUser(id, user);
    }

    @Override
    public void deleteUser(Long id) {
        userService.deleteUser(id);
    }

    @Override
    public User login(String username, String password) {
        Staff staff = staffDao.findByUsername(username);
        if (staff == null) {
            throw new EntityNotFoundException("Staff not found with username: " + username);
        }
        
        if (!staff.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        
        return staff;
    }
} 