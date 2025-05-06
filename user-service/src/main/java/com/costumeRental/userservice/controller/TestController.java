package com.costumeRental.userservice.controller;

import com.costumeRental.userservice.dao.UserDao;
import com.costumeRental.userservice.dao.CustomerDao;
import com.costumeRental.userservice.dao.StaffDao;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final UserDao userDao;
    private final CustomerDao customerDao;
    private final StaffDao staffDao;

    @GetMapping("/counts")
    public ResponseEntity<Map<String, Integer>> getCounts() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("usersCount", userDao.findAll().size());
        counts.put("customersCount", customerDao.findAll().size());
        counts.put("staffCount", staffDao.findAll().size());
        return ResponseEntity.ok(counts);
    }
} 