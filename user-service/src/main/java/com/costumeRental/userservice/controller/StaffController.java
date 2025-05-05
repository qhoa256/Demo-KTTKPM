package com.costumeRental.userservice.controller;

import com.costumeRental.userservice.model.Staff;
import com.costumeRental.userservice.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffRepository staffRepository;

    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        return ResponseEntity.ok(staffRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Long id) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Staff not found with id: " + id));
        return ResponseEntity.ok(staff);
    }
    
    @GetMapping("/manager/{managerCode}")
    public ResponseEntity<Staff> getStaffByManagerCode(@PathVariable String managerCode) {
        Staff staff = staffRepository.findByManagerCode(managerCode);
        if (staff == null) {
            throw new EntityNotFoundException("Staff not found with manager code: " + managerCode);
        }
        return ResponseEntity.ok(staff);
    }

    @PostMapping
    public ResponseEntity<Staff> createStaff(@RequestBody Staff staff) {
        return new ResponseEntity<>(staffRepository.save(staff), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Staff> updateStaff(@PathVariable Long id, @RequestBody Staff staff) {
        if (!staffRepository.existsById(id)) {
            throw new EntityNotFoundException("Staff not found with id: " + id);
        }
        staff.setId(id);
        return ResponseEntity.ok(staffRepository.save(staff));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        if (!staffRepository.existsById(id)) {
            throw new EntityNotFoundException("Staff not found with id: " + id);
        }
        staffRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
} 