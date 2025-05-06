package com.costumeRental.userservice.dao.impl;

import com.costumeRental.userservice.dao.StaffDao;
import com.costumeRental.userservice.model.Address;
import com.costumeRental.userservice.model.FullName;
import com.costumeRental.userservice.model.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class StaffDaoImpl implements StaffDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDaoImpl userDao;

    @Autowired
    public StaffDaoImpl(JdbcTemplate jdbcTemplate, UserDaoImpl userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    @Override
    public Staff save(Staff staff) {
        if (staff.getId() == null) {
            // Save the user part first
            // Save FullName first
            if (staff.getFullName() != null) {
                Long fullNameId = saveFullName(staff.getFullName());
                staff.getFullName().setId(fullNameId);
            }
            
            // Save Address
            if (staff.getAddress() != null) {
                Long addressId = saveAddress(staff.getAddress());
                staff.getAddress().setId(addressId);
            }
            
            // Insert into tblUser
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO tblUser (username, password, AddressId, FullNameId, Discriminator) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, staff.getUsername());
                ps.setString(2, staff.getPassword());
                ps.setObject(3, staff.getAddress() != null ? staff.getAddress().getId() : null);
                ps.setObject(4, staff.getFullName() != null ? staff.getFullName().getId() : null);
                ps.setString(5, "STAFF");
                return ps;
            }, keyHolder);
            
            staff.setId((Long) keyHolder.getKey());
            
            // Insert into Staff table
            jdbcTemplate.update(
                    "INSERT INTO Staff (id, position) VALUES (?, ?)",
                    staff.getId(),
                    staff.getPosition()
            );
        } else {
            // Update staff
            // Update the user part first
            if (staff.getFullName() != null) {
                if (staff.getFullName().getId() == null) {
                    Long fullNameId = saveFullName(staff.getFullName());
                    staff.getFullName().setId(fullNameId);
                } else {
                    updateFullName(staff.getFullName());
                }
            }
            
            if (staff.getAddress() != null) {
                if (staff.getAddress().getId() == null) {
                    Long addressId = saveAddress(staff.getAddress());
                    staff.getAddress().setId(addressId);
                } else {
                    updateAddress(staff.getAddress());
                }
            }
            
            // Update tblUser
            jdbcTemplate.update(
                    "UPDATE tblUser SET username = ?, password = ?, AddressId = ?, FullNameId = ? WHERE id = ?",
                    staff.getUsername(),
                    staff.getPassword(),
                    staff.getAddress() != null ? staff.getAddress().getId() : null,
                    staff.getFullName() != null ? staff.getFullName().getId() : null,
                    staff.getId()
            );
            
            // Update Staff table
            jdbcTemplate.update(
                    "UPDATE Staff SET position = ? WHERE id = ?",
                    staff.getPosition(),
                    staff.getId()
            );
        }
        return staff;
    }

    @Override
    public Optional<Staff> findById(Long id) {
        List<Staff> staffs = jdbcTemplate.query(
                "SELECT u.*, s.position, a.id as address_id, a.street, a.city, fn.id as fullname_id, fn.firstName, fn.lastName " +
                "FROM tblUser u " +
                "INNER JOIN Staff s ON u.id = s.id " +
                "LEFT JOIN Address a ON u.AddressId = a.id " +
                "LEFT JOIN FullName fn ON u.FullNameId = fn.id " +
                "WHERE u.id = ?",
                staffRowMapper(),
                id
        );
        return staffs.isEmpty() ? Optional.empty() : Optional.of(staffs.get(0));
    }

    @Override
    public Staff findByUsername(String username) {
        List<Staff> staffs = jdbcTemplate.query(
                "SELECT u.*, s.position, a.id as address_id, a.street, a.city, fn.id as fullname_id, fn.firstName, fn.lastName " +
                "FROM tblUser u " +
                "INNER JOIN Staff s ON u.id = s.id " +
                "LEFT JOIN Address a ON u.AddressId = a.id " +
                "LEFT JOIN FullName fn ON u.FullNameId = fn.id " +
                "WHERE u.username = ?",
                staffRowMapper(),
                username
        );
        return staffs.isEmpty() ? null : staffs.get(0);
    }

    @Override
    public List<Staff> findAll() {
        return jdbcTemplate.query(
                "SELECT u.*, s.position, a.id as address_id, a.street, a.city, fn.id as fullname_id, fn.firstName, fn.lastName " +
                "FROM tblUser u " +
                "INNER JOIN Staff s ON u.id = s.id " +
                "LEFT JOIN Address a ON u.AddressId = a.id " +
                "LEFT JOIN FullName fn ON u.FullNameId = fn.id",
                staffRowMapper()
        );
    }

    @Override
    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Staff WHERE id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    @Override
    public void deleteById(Long id) {
        // First delete from Staff table
        jdbcTemplate.update("DELETE FROM Staff WHERE id = ?", id);
        
        // Then delete from tblUser and related tables
        Optional<Staff> staffOpt = findById(id);
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            
            // Delete the user first
            jdbcTemplate.update("DELETE FROM tblUser WHERE id = ?", id);
            
            // Delete associated fullname if exists
            if (staff.getFullName() != null && staff.getFullName().getId() != null) {
                jdbcTemplate.update("DELETE FROM FullName WHERE id = ?", staff.getFullName().getId());
            }
            
            // Delete associated address if exists
            if (staff.getAddress() != null && staff.getAddress().getId() != null) {
                jdbcTemplate.update("DELETE FROM Address WHERE id = ?", staff.getAddress().getId());
            }
        }
    }

    private RowMapper<Staff> staffRowMapper() {
        return (rs, rowNum) -> {
            Staff staff = new Staff();
            staff.setId(rs.getLong("id"));
            staff.setUsername(rs.getString("username"));
            staff.setPassword(rs.getString("password"));
            staff.setPosition(rs.getString("position"));
            
            // Handle Address
            Long addressId = rs.getObject("address_id", Long.class);
            if (addressId != null) {
                Address address = new Address();
                address.setId(addressId);
                address.setStreet(rs.getString("street"));
                address.setCity(rs.getString("city"));
                address.setUser(staff);
                staff.setAddress(address);
            }
            
            // Handle FullName
            Long fullNameId = rs.getObject("fullname_id", Long.class);
            if (fullNameId != null) {
                FullName fullName = new FullName();
                fullName.setId(fullNameId);
                fullName.setFirstName(rs.getString("firstName"));
                fullName.setLastName(rs.getString("lastName"));
                fullName.setUser(staff);
                staff.setFullName(fullName);
            }
            
            return staff;
        };
    }

    private Long saveFullName(FullName fullName) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO FullName (firstName, lastName) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, fullName.getFirstName());
            ps.setString(2, fullName.getLastName());
            return ps;
        }, keyHolder);
        return (Long) keyHolder.getKey();
    }

    private void updateFullName(FullName fullName) {
        jdbcTemplate.update(
                "UPDATE FullName SET firstName = ?, lastName = ? WHERE id = ?",
                fullName.getFirstName(),
                fullName.getLastName(),
                fullName.getId()
        );
    }

    private Long saveAddress(Address address) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Address (street, city) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, address.getStreet());
            ps.setString(2, address.getCity());
            return ps;
        }, keyHolder);
        return (Long) keyHolder.getKey();
    }

    private void updateAddress(Address address) {
        jdbcTemplate.update(
                "UPDATE Address SET street = ?, city = ? WHERE id = ?",
                address.getStreet(),
                address.getCity(),
                address.getId()
        );
    }
} 