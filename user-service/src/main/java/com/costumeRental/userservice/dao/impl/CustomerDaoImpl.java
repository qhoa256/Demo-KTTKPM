package com.costumeRental.userservice.dao.impl;

import com.costumeRental.userservice.dao.CustomerDao;
import com.costumeRental.userservice.model.Address;
import com.costumeRental.userservice.model.Customer;
import com.costumeRental.userservice.model.FullName;
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
public class CustomerDaoImpl implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDaoImpl userDao;

    @Autowired
    public CustomerDaoImpl(JdbcTemplate jdbcTemplate, UserDaoImpl userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
    }

    @Override
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            // Save the user part first
            // Save FullName first
            if (customer.getFullName() != null) {
                Long fullNameId = saveFullName(customer.getFullName());
                customer.getFullName().setId(fullNameId);
            }
            
            // Save Address
            if (customer.getAddress() != null) {
                Long addressId = saveAddress(customer.getAddress());
                customer.getAddress().setId(addressId);
            }
            
            // Insert into tblUser
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO tblUser (username, password, AddressId, FullNameId, Discriminator) VALUES (?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, customer.getUsername());
                ps.setString(2, customer.getPassword());
                ps.setObject(3, customer.getAddress() != null ? customer.getAddress().getId() : null);
                ps.setObject(4, customer.getFullName() != null ? customer.getFullName().getId() : null);
                ps.setString(5, "CUSTOMER");
                return ps;
            }, keyHolder);
            
            customer.setId((Long) keyHolder.getKey());
            
            // Insert into Customer table
            jdbcTemplate.update(
                    "INSERT INTO Customer (id, loyaltyPoints) VALUES (?, ?)",
                    customer.getId(),
                    customer.getLoyaltyPoints()
            );
        } else {
            // Update customer
            // Update the user part first
            if (customer.getFullName() != null) {
                if (customer.getFullName().getId() == null) {
                    Long fullNameId = saveFullName(customer.getFullName());
                    customer.getFullName().setId(fullNameId);
                } else {
                    updateFullName(customer.getFullName());
                }
            }
            
            if (customer.getAddress() != null) {
                if (customer.getAddress().getId() == null) {
                    Long addressId = saveAddress(customer.getAddress());
                    customer.getAddress().setId(addressId);
                } else {
                    updateAddress(customer.getAddress());
                }
            }
            
            // Update tblUser
            jdbcTemplate.update(
                    "UPDATE tblUser SET username = ?, password = ?, AddressId = ?, FullNameId = ? WHERE id = ?",
                    customer.getUsername(),
                    customer.getPassword(),
                    customer.getAddress() != null ? customer.getAddress().getId() : null,
                    customer.getFullName() != null ? customer.getFullName().getId() : null,
                    customer.getId()
            );
            
            // Update Customer table
            jdbcTemplate.update(
                    "UPDATE Customer SET loyaltyPoints = ? WHERE id = ?",
                    customer.getLoyaltyPoints(),
                    customer.getId()
            );
        }
        return customer;
    }

    @Override
    public Optional<Customer> findById(Long id) {
        List<Customer> customers = jdbcTemplate.query(
                "SELECT u.*, c.loyaltyPoints, a.id as address_id, a.street, a.city, fn.id as fullname_id, fn.firstName, fn.lastName " +
                "FROM tblUser u " +
                "INNER JOIN Customer c ON u.id = c.id " +
                "LEFT JOIN Address a ON u.AddressId = a.id " +
                "LEFT JOIN FullName fn ON u.FullNameId = fn.id " +
                "WHERE u.id = ?",
                customerRowMapper(),
                id
        );
        return customers.isEmpty() ? Optional.empty() : Optional.of(customers.get(0));
    }

    @Override
    public Customer findByUsername(String username) {
        List<Customer> customers = jdbcTemplate.query(
                "SELECT u.*, c.loyaltyPoints, a.id as address_id, a.street, a.city, fn.id as fullname_id, fn.firstName, fn.lastName " +
                "FROM tblUser u " +
                "INNER JOIN Customer c ON u.id = c.id " +
                "LEFT JOIN Address a ON u.AddressId = a.id " +
                "LEFT JOIN FullName fn ON u.FullNameId = fn.id " +
                "WHERE u.username = ?",
                customerRowMapper(),
                username
        );
        return customers.isEmpty() ? null : customers.get(0);
    }

    @Override
    public List<Customer> findAll() {
        return jdbcTemplate.query(
                "SELECT u.*, c.loyaltyPoints, a.id as address_id, a.street, a.city, fn.id as fullname_id, fn.firstName, fn.lastName " +
                "FROM tblUser u " +
                "INNER JOIN Customer c ON u.id = c.id " +
                "LEFT JOIN Address a ON u.AddressId = a.id " +
                "LEFT JOIN FullName fn ON u.FullNameId = fn.id",
                customerRowMapper()
        );
    }

    @Override
    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM Customer WHERE id = ?",
                Integer.class,
                id
        );
        return count != null && count > 0;
    }

    @Override
    public void deleteById(Long id) {
        // First delete from Customer table
        jdbcTemplate.update("DELETE FROM Customer WHERE id = ?", id);
        
        // Then delete from tblUser and related tables
        Optional<Customer> customerOpt = findById(id);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            
            // Delete the user first
            jdbcTemplate.update("DELETE FROM tblUser WHERE id = ?", id);
            
            // Delete associated fullname if exists
            if (customer.getFullName() != null && customer.getFullName().getId() != null) {
                jdbcTemplate.update("DELETE FROM FullName WHERE id = ?", customer.getFullName().getId());
            }
            
            // Delete associated address if exists
            if (customer.getAddress() != null && customer.getAddress().getId() != null) {
                jdbcTemplate.update("DELETE FROM Address WHERE id = ?", customer.getAddress().getId());
            }
        }
    }

    private RowMapper<Customer> customerRowMapper() {
        return (rs, rowNum) -> {
            Customer customer = new Customer();
            customer.setId(rs.getLong("id"));
            customer.setUsername(rs.getString("username"));
            customer.setPassword(rs.getString("password"));
            customer.setLoyaltyPoints(rs.getInt("loyaltyPoints"));
            
            // Handle Address
            Long addressId = rs.getObject("address_id", Long.class);
            if (addressId != null) {
                Address address = new Address();
                address.setId(addressId);
                address.setStreet(rs.getString("street"));
                address.setCity(rs.getString("city"));
                address.setUser(customer);
                customer.setAddress(address);
            }
            
            // Handle FullName
            Long fullNameId = rs.getObject("fullname_id", Long.class);
            if (fullNameId != null) {
                FullName fullName = new FullName();
                fullName.setId(fullNameId);
                fullName.setFirstName(rs.getString("firstName"));
                fullName.setLastName(rs.getString("lastName"));
                fullName.setUser(customer);
                customer.setFullName(fullName);
            }
            
            return customer;
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