package com.costumeRental.billcostumeservice.dao.impl;

import com.costumeRental.billcostumeservice.exception.ResourceNotFoundException;
import com.costumeRental.billcostumeservice.model.Bill;
import com.costumeRental.billcostumeservice.model.Payment;
import com.costumeRental.billcostumeservice.dao.BillDao;
import com.costumeRental.billcostumeservice.dao.PaymentDao;
import com.costumeRental.billcostumeservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class BillDaoImpl implements BillDao {
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    private PaymentDao paymentDao;
    
    @Autowired
    private CustomerService customerService;

    @Autowired
    public BillDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Bill> billRowMapper = (rs, rowNum) -> {
        Bill bill = new Bill();
        bill.setId(rs.getLong("id"));
        
        // Fetch customer from user-service
        Long customerId = rs.getLong("customer_id");
        if (customerId != 0) {
            bill.setCustomer(customerService.getCustomerById(customerId));
        }
        
        bill.setRentDate(rs.getDate("rent_date") != null ? rs.getDate("rent_date").toLocalDate() : null);
        bill.setReturnDate(rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null);
        bill.setNote(rs.getString("note"));
        bill.setAddress(rs.getString("address"));
        
        Long paymentId = rs.getLong("payment_id");
        if (paymentId != 0) {
            paymentDao.findById(paymentId).ifPresent(bill::setPayment);
        }
        
        return bill;
    };

    @Override
    public List<Bill> findAll() {
        String sql = "SELECT * FROM tblBill";
        return jdbcTemplate.query(sql, billRowMapper);
    }

    @Override
    public Optional<Bill> findById(Long id) {
        try {
            String sql = "SELECT * FROM tblBill WHERE id = ?";
            Bill bill = jdbcTemplate.queryForObject(sql, new Object[]{id}, billRowMapper);
            return Optional.ofNullable(bill);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Bill save(Bill bill) {
        if (bill.getId() == null) {
            return insert(bill);
        } else {
            return update(bill);
        }
    }

    private Bill insert(Bill bill) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO tblBill (customer_id, rent_date, return_date, payment_id, note, address) VALUES (?, ?, ?, ?, ?, ?)";

        // First save the payment if it exists
        Long paymentId = null;
        if (bill.getPayment() != null) {
            Payment savedPayment = paymentDao.save(bill.getPayment());
            paymentId = savedPayment.getId();
            bill.setPayment(savedPayment);
        }
        
        // Create a final copy of paymentId to use in the lambda
        final Long finalPaymentId = paymentId;

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            // Use customer ID from the Customer object
            if (bill.getCustomer() != null && bill.getCustomer().getId() != null) {
                ps.setLong(1, bill.getCustomer().getId());
            } else {
                ps.setNull(1, Types.BIGINT);
            }
            
            if (bill.getRentDate() != null) {
                ps.setDate(2, java.sql.Date.valueOf(bill.getRentDate()));
            } else {
                ps.setNull(2, Types.DATE);
            }
            
            if (bill.getReturnDate() != null) {
                ps.setDate(3, java.sql.Date.valueOf(bill.getReturnDate()));
            } else {
                ps.setNull(3, Types.DATE);
            }
            
            if (finalPaymentId != null) {
                ps.setLong(4, finalPaymentId);
            } else {
                ps.setNull(4, Types.BIGINT);
            }
            
            ps.setString(5, bill.getNote());
            ps.setString(6, bill.getAddress());
            
            return ps;
        }, keyHolder);

        bill.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return bill;
    }

    private Bill update(Bill bill) {
        // First update the payment if it exists
        if (bill.getPayment() != null) {
            paymentDao.save(bill.getPayment());
        }

        String sql = "UPDATE tblBill SET customer_id = ?, rent_date = ?, return_date = ?, payment_id = ?, note = ?, address = ? WHERE id = ?";
        int updated = jdbcTemplate.update(sql,
                bill.getCustomer() != null ? bill.getCustomer().getId() : null,
                bill.getRentDate() != null ? java.sql.Date.valueOf(bill.getRentDate()) : null,
                bill.getReturnDate() != null ? java.sql.Date.valueOf(bill.getReturnDate()) : null,
                bill.getPayment() != null ? bill.getPayment().getId() : null,
                bill.getNote(),
                bill.getAddress(),
                bill.getId());

        if (updated == 0) {
            throw new ResourceNotFoundException("Bill not found with id: " + bill.getId());
        }

        return bill;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM tblBill WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        
        if (deleted == 0) {
            throw new ResourceNotFoundException("Bill not found with id: " + id);
        }
    }
} 