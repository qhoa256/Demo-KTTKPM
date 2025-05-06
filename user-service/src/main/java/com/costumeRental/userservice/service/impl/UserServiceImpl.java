package com.costumeRental.userservice.service.impl;

import com.costumeRental.userservice.dao.UserDao;
import com.costumeRental.userservice.model.Address;
import com.costumeRental.userservice.model.FullName;
import com.costumeRental.userservice.model.User;
import com.costumeRental.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    public User createUser(User user) {
        return userDao.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User not found with username: " + username);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    public User updateUser(Long id, User userDetails) {
        User existingUser = userDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        
        existingUser.setUsername(userDetails.getUsername());
        existingUser.setPassword(userDetails.getPassword());
        
        if (userDetails.getAddress() != null) {
            Address address = existingUser.getAddress();
            if (address == null) {
                address = new Address();
                address.setUser(existingUser);
                existingUser.setAddress(address);
            }
            address.setCity(userDetails.getAddress().getCity());
            address.setStreet(userDetails.getAddress().getStreet());
        }
        
        if (userDetails.getFullName() != null) {
            FullName fullName = existingUser.getFullName();
            if (fullName == null) {
                fullName = new FullName();
                fullName.setUser(existingUser);
                existingUser.setFullName(fullName);
            }
            fullName.setFirstName(userDetails.getFullName().getFirstName());
            fullName.setLastName(userDetails.getFullName().getLastName());
        }
        
        return userDao.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userDao.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userDao.deleteById(id);
    }
    
    @Override
    public User login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User not found with username: " + username);
        }
        
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        
        return user;
    }
} 