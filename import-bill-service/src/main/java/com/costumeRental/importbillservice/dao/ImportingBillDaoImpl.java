package com.costumeRental.importbillservice.dao;

import com.costumeRental.importbillservice.model.ImportingBill;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ImportingBillDaoImpl implements ImportingBillDao {

    private final DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    private JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        return jdbcTemplate;
    }

    private final RowMapper<ImportingBill> importingBillRowMapper = (rs, rowNum) -> {
        ImportingBill importingBill = new ImportingBill();
        importingBill.setId(rs.getLong("id"));
        importingBill.setManagerId(rs.getLong("managerId"));
        importingBill.setSupplierId(rs.getLong("supplierId"));
        Date importDate = rs.getDate("importDate");
        if (importDate != null) {
            importingBill.setImportDate(importDate.toLocalDate());
        }
        return importingBill;
    };

    @Override
    public List<ImportingBill> findAll() {
        String sql = "SELECT * FROM tblImportingBill";
        return getJdbcTemplate().query(sql, importingBillRowMapper);
    }

    @Override
    public Optional<ImportingBill> findById(Long id) {
        try {
            String sql = "SELECT * FROM tblImportingBill WHERE id = ?";
            ImportingBill importingBill = getJdbcTemplate().queryForObject(sql, importingBillRowMapper, id);
            return Optional.ofNullable(importingBill);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public ImportingBill save(ImportingBill importingBill) {
        if (importingBill.getId() == null) {
            return insert(importingBill);
        } else {
            return update(importingBill);
        }
    }

    private ImportingBill insert(ImportingBill importingBill) {
        String sql = "INSERT INTO tblImportingBill (managerId, supplierId, importDate) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        getJdbcTemplate().update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, importingBill.getManagerId());
            ps.setLong(2, importingBill.getSupplierId());
            ps.setDate(3, importingBill.getImportDate() != null ? 
                    Date.valueOf(importingBill.getImportDate()) : null);
            return ps;
        }, keyHolder);
        
        importingBill.setId(keyHolder.getKey().longValue());
        return importingBill;
    }

    private ImportingBill update(ImportingBill importingBill) {
        String sql = "UPDATE tblImportingBill SET managerId = ?, supplierId = ?, importDate = ? WHERE id = ?";
        getJdbcTemplate().update(sql, 
                importingBill.getManagerId(),
                importingBill.getSupplierId(),
                importingBill.getImportDate() != null ? Date.valueOf(importingBill.getImportDate()) : null,
                importingBill.getId());
        return importingBill;
    }

    @Override
    public void delete(ImportingBill importingBill) {
        String sql = "DELETE FROM tblImportingBill WHERE id = ?";
        getJdbcTemplate().update(sql, importingBill.getId());
    }
} 