package com.costumeRental.costumeservice.dao.impl;

import com.costumeRental.costumeservice.dao.CostumeSupplierDao;
import com.costumeRental.costumeservice.model.Costume;
import com.costumeRental.costumeservice.model.CostumeSupplier;
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

@Repository
public class CostumeSupplierDaoImpl implements CostumeSupplierDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<CostumeSupplier> costumeSupplierRowMapper = (rs, rowNum) -> {
        CostumeSupplier costumeSupplier = new CostumeSupplier();
        costumeSupplier.setId(rs.getLong("id"));
        costumeSupplier.setSupplierId(rs.getString("supplier_id"));
        
        Costume costume = new Costume();
        costume.setId(rs.getLong("costume_id"));
        costumeSupplier.setCostume(costume);
        
        return costumeSupplier;
    };

    @Autowired
    public CostumeSupplierDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CostumeSupplier> findAll() {
        String sql = "SELECT * FROM tblCostumeSupplier";
        List<CostumeSupplier> suppliers = jdbcTemplate.query(sql, costumeSupplierRowMapper);
        suppliers.forEach(this::loadCostumeDetails);
        return suppliers;
    }

    @Override
    public CostumeSupplier findById(Long id) {
        try {
            String sql = "SELECT * FROM tblCostumeSupplier WHERE id = ?";
            CostumeSupplier supplier = jdbcTemplate.queryForObject(sql, costumeSupplierRowMapper, id);
            if (supplier != null) {
                loadCostumeDetails(supplier);
            }
            return supplier;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<CostumeSupplier> findBySupplierId(String supplierId) {
        String sql = "SELECT * FROM tblCostumeSupplier WHERE supplier_id = ?";
        List<CostumeSupplier> suppliers = jdbcTemplate.query(sql, costumeSupplierRowMapper, supplierId);
        suppliers.forEach(this::loadCostumeDetails);
        return suppliers;
    }

    @Override
    public CostumeSupplier save(CostumeSupplier costumeSupplier) {
        if (costumeSupplier.getId() == null) {
            // Insert new costume supplier
            return insertCostumeSupplier(costumeSupplier);
        } else {
            // Update existing costume supplier
            return updateCostumeSupplier(costumeSupplier);
        }
    }

    private CostumeSupplier insertCostumeSupplier(CostumeSupplier costumeSupplier) {
        String sql = "INSERT INTO tblCostumeSupplier (costume_id, supplier_id) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, costumeSupplier.getCostume().getId());
            ps.setString(2, costumeSupplier.getSupplierId());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        costumeSupplier.setId(id);
        return costumeSupplier;
    }

    private CostumeSupplier updateCostumeSupplier(CostumeSupplier costumeSupplier) {
        String sql = "UPDATE tblCostumeSupplier SET costume_id = ?, supplier_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, 
            costumeSupplier.getCostume().getId(), 
            costumeSupplier.getSupplierId(),
            costumeSupplier.getId()
        );
        return costumeSupplier;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM tblCostumeSupplier WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    private void loadCostumeDetails(CostumeSupplier costumeSupplier) {
        String sql = "SELECT * FROM tblCostume WHERE id = ?";
        try {
            Costume costume = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Costume c = new Costume();
                c.setId(rs.getLong("id"));
                c.setCategory(rs.getString("category"));
                c.setName(rs.getString("name"));
                c.setDescription(rs.getString("description"));
                c.setPrice(rs.getBigDecimal("price"));
                return c;
            }, costumeSupplier.getCostume().getId());
            costumeSupplier.setCostume(costume);
        } catch (EmptyResultDataAccessException e) {
            // Leave the costume with only ID set
        }
    }
} 