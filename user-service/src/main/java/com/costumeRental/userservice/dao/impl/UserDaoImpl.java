package com.costumeRental.userservice.dao.impl;

import com.costumeRental.userservice.dao.UserDao;
import com.costumeRental.userservice.model.Address;
import com.costumeRental.userservice.model.FullName;
import com.costumeRental.userservice.model.User;
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
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            // Insert new user
            KeyHolder keyHolder = new GeneratedKeyHolder();
            
            // Save FullName first
            if (user.getFullName() != null) {
                Long fullNameId = saveFullName(user.getFullName());
                user.getFullName().setId(fullNameId);
            }
            
            // Save Address
            if (user.getAddress() != null) {
                Long addressId = saveAddress(user.getAddress());
                user.getAddress().setId(addressId);
            }
            
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO tblUser (username, password, AddressId, FullNameId, Discriminator) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setObject(3, user.getAddress() != null ? user.getAddress().getId() : null);
                ps.setObject(4, user.getFullName() != null ? user.getFullName().getId() : null);
                ps.setString(5, "USER");
                return ps;
            }, keyHolder);
            
            user.setId((Long) keyHolder.getKey());
        } else {
            // Update existing user
            if (user.getFullName() != null) {
                if (user.getFullName().getId() == null) {
                    Long fullNameId = saveFullName(user.getFullName());
                    user.getFullName().setId(fullNameId);
                } else {
                    updateFullName(user.getFullName());
                }
            }
            
            if (user.getAddress() != null) {
                if (user.getAddress().getId() == null) {
                    Long addressId = saveAddress(user.getAddress());
                    user.getAddress().setId(addressId);
                } else {
                    updateAddress(user.getAddress());
                }
            }
            
            jdbcTemplate.update(
                    "UPDATE tblUser SET username = ?, password = ?, AddressId = ?, FullNameId = ? WHERE id = ?",
                    user.getUsername(), 
                    user.getPassword(),
                    user.getAddress() != null ? user.getAddress().getId() : null,
                    user.getFullName() != null ? user.getFullName().getId() : null,
                    user.getId()
            );
        }
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> users = jdbcTemplate.query(
                "SELECT u.*, a.id as address_id, a.street, a.city, fn.id as fullname_id, fn.firstName, fn.lastName " +
                "FROM tblUser u " +
                "LEFT JOIN Address a ON u.AddressId = a.id " +
                "LEFT JOIN FullName fn ON u.FullNameId = fn.id " +
                "WHERE u.id = ?",
                userRowMapper(),
                id
        );
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    @Override
    public User findByUsername(String username) {
        List<User> users = jdbcTemplate.query(
                "SELECT u.*, a.id as address_id, a.street, a.city, fn.id as fullname_id, fn.firstName, fn.lastName " +
                "FROM tblUser u " +
                "LEFT JOIN Address a ON u.AddressId = a.id " +
                "LEFT JOIN FullName fn ON u.FullNameId = fn.id " +
                "WHERE u.username = ?",
                userRowMapper(),
                username
        );
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(
                "SELECT u.*, a.id as address_id, a.street, a.city, fn.id as fullname_id, fn.firstName, fn.lastName " +
                "FROM tblUser u " +
                "LEFT JOIN Address a ON u.AddressId = a.id " +
                "LEFT JOIN FullName fn ON u.FullNameId = fn.id " +
                "WHERE u.Discriminator = 'USER' or u.Discriminator = 'STAFF' or u.Discriminator = 'CUSTOMER'",
                userRowMapper()
        );
    }

    @Override
    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM tblUser WHERE id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    @Override
    public void deleteById(Long id) {
        // First, get the user to find associated entities
        Optional<User> userOpt = findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            
            // Delete the user first
            jdbcTemplate.update("DELETE FROM tblUser WHERE id = ?", id);
            
            // Delete associated fullname if exists
            if (user.getFullName() != null && user.getFullName().getId() != null) {
                jdbcTemplate.update("DELETE FROM FullName WHERE id = ?", user.getFullName().getId());
            }
            
            // Delete associated address if exists
            if (user.getAddress() != null && user.getAddress().getId() != null) {
                jdbcTemplate.update("DELETE FROM Address WHERE id = ?", user.getAddress().getId());
            }
        }
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            
            // Handle Address
            Long addressId = rs.getObject("address_id", Long.class);
            if (addressId != null) {
                Address address = new Address();
                address.setId(addressId);
                address.setStreet(rs.getString("street"));
                address.setCity(rs.getString("city"));
                address.setUser(user);
                user.setAddress(address);
            }
            
            // Handle FullName
            Long fullNameId = rs.getObject("fullname_id", Long.class);
            if (fullNameId != null) {
                FullName fullName = new FullName();
                fullName.setId(fullNameId);
                fullName.setFirstName(rs.getString("firstName"));
                fullName.setLastName(rs.getString("lastName"));
                fullName.setUser(user);
                user.setFullName(fullName);
            }
            
            return user;
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