package com.costumeRental.billcostumeservice.dao.impl;

import com.costumeRental.billcostumeservice.exception.ResourceNotFoundException;
import com.costumeRental.billcostumeservice.model.Payment;
import com.costumeRental.billcostumeservice.dao.PaymentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class PaymentDaoImpl implements PaymentDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PaymentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Payment> paymentRowMapper = (rs, rowNum) -> {
        Payment payment = new Payment();
        payment.setId(rs.getLong("id"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setPaymentNote(rs.getString("payment_note"));
        return payment;
    };

    @Override
    public List<Payment> findAll() {
        String sql = "SELECT * FROM tblPayment";
        return jdbcTemplate.query(sql, paymentRowMapper);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        try {
            String sql = "SELECT * FROM tblPayment WHERE id = ?";
            Payment payment = jdbcTemplate.queryForObject(sql, new Object[]{id}, paymentRowMapper);
            return Optional.ofNullable(payment);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Payment save(Payment payment) {
        if (payment.getId() == null) {
            return insert(payment);
        } else {
            return update(payment);
        }
    }

    private Payment insert(Payment payment) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO tblPayment (payment_method, payment_note) VALUES (?, ?)";

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, payment.getPaymentMethod());
            ps.setString(2, payment.getPaymentNote());
            return ps;
        }, keyHolder);

        payment.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return payment;
    }

    private Payment update(Payment payment) {
        String sql = "UPDATE tblPayment SET payment_method = ?, payment_note = ? WHERE id = ?";
        int updated = jdbcTemplate.update(sql,
                payment.getPaymentMethod(),
                payment.getPaymentNote(),
                payment.getId());

        if (updated == 0) {
            throw new ResourceNotFoundException("Payment not found with id: " + payment.getId());
        }

        return payment;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM tblPayment WHERE id = ?";
        int deleted = jdbcTemplate.update(sql, id);
        
        if (deleted == 0) {
            throw new ResourceNotFoundException("Payment not found with id: " + id);
        }
    }
} 