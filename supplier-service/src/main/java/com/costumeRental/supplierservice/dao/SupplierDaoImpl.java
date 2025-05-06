package com.costumeRental.supplierservice.dao;

import com.costumeRental.supplierservice.model.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SupplierDaoImpl implements SupplierDao {

    private final DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    private JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        return jdbcTemplate;
    }

    private final RowMapper<Supplier> supplierRowMapper = (rs, rowNum) -> {
        Supplier supplier = new Supplier();
        supplier.setId(rs.getLong("id"));
        supplier.setEmail(rs.getString("email"));
        supplier.setContact(rs.getString("contact"));
        return supplier;
    };

    @Override
    public List<Supplier> findAll() {
        String sql = "SELECT * FROM tblSupplier";
        return getJdbcTemplate().query(sql, supplierRowMapper);
    }

    @Override
    public Optional<Supplier> findById(Long id) {
        try {
            String sql = "SELECT * FROM tblSupplier WHERE id = ?";
            Supplier supplier = getJdbcTemplate().queryForObject(sql, supplierRowMapper, id);
            return Optional.ofNullable(supplier);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Supplier save(Supplier supplier) {
        if (supplier.getId() == null) {
            return insert(supplier);
        } else {
            return update(supplier);
        }
    }

    private Supplier insert(Supplier supplier) {
        String sql = "INSERT INTO tblSupplier (email, contact) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, supplier.getEmail());
            ps.setString(2, supplier.getContact());
            return ps;
        }, keyHolder);
        
        supplier.setId(keyHolder.getKey().longValue());
        return supplier;
    }

    private Supplier update(Supplier supplier) {
        String sql = "UPDATE tblSupplier SET email = ?, contact = ? WHERE id = ?";
        getJdbcTemplate().update(sql, supplier.getEmail(), supplier.getContact(), supplier.getId());
        return supplier;
    }

    @Override
    public void delete(Supplier supplier) {
        String sql = "DELETE FROM tblSupplier WHERE id = ?";
        getJdbcTemplate().update(sql, supplier.getId());
    }
} 