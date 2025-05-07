package com.costumeRental.costumeservice.dao.impl;

import com.costumeRental.costumeservice.dao.CostumeDao;
import com.costumeRental.costumeservice.dao.CostumeImportingBillDao;
import com.costumeRental.costumeservice.model.CostumeImportingBill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class CostumeImportingBillDaoImpl implements CostumeImportingBillDao {

    private final JdbcTemplate jdbcTemplate;
    private final CostumeDao costumeDao;
    private final RowMapper<CostumeImportingBill> costumeImportingBillRowMapper;

    @Autowired
    public CostumeImportingBillDaoImpl(JdbcTemplate jdbcTemplate, @Lazy CostumeDao costumeDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.costumeDao = costumeDao;
        this.costumeImportingBillRowMapper = (rs, rowNum) -> {
            CostumeImportingBill costumeImportingBill = new CostumeImportingBill();
            costumeImportingBill.setId(rs.getLong("id"));
            
            Long costumeId = rs.getLong("costume_id");
            if (costumeId != 0) {
                costumeDao.findById(costumeId).ifPresent(costumeImportingBill::setCostume);
            }
            
            costumeImportingBill.setImportPrice(rs.getBigDecimal("import_price"));
            costumeImportingBill.setQuantity(rs.getInt("quantity"));
            costumeImportingBill.setName(rs.getString("name"));
            costumeImportingBill.setDescription(rs.getString("description"));
            return costumeImportingBill;
        };
    }

    @Override
    public List<CostumeImportingBill> findAll() {
        String sql = "SELECT * FROM tblCostumeImportingBill";
        return jdbcTemplate.query(sql, costumeImportingBillRowMapper);
    }

    @Override
    public Optional<CostumeImportingBill> findById(Long id) {
        try {
            String sql = "SELECT * FROM tblCostumeImportingBill WHERE id = ?";
            CostumeImportingBill costumeImportingBill = jdbcTemplate.queryForObject(sql, costumeImportingBillRowMapper, id);
            return Optional.ofNullable(costumeImportingBill);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CostumeImportingBill> findByCostumeId(Long costumeId) {
        String sql = "SELECT * FROM tblCostumeImportingBill WHERE costume_id = ?";
        return jdbcTemplate.query(sql, costumeImportingBillRowMapper, costumeId);
    }

    @Override
    public CostumeImportingBill save(CostumeImportingBill costumeImportingBill) {
        if (costumeImportingBill.getId() == null) {
            // Insert new costume importing bill
            return insertCostumeImportingBill(costumeImportingBill);
        } else {
            // Update existing costume importing bill
            return updateCostumeImportingBill(costumeImportingBill);
        }
    }

    private CostumeImportingBill insertCostumeImportingBill(CostumeImportingBill costumeImportingBill) {
        String sql = "INSERT INTO tblCostumeImportingBill (costume_id, import_price, quantity, name, description) " +
                     "VALUES (?, ?, ?, ?, ?)";
                     
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, costumeImportingBill.getCostume() != null ? costumeImportingBill.getCostume().getId() : null);
            ps.setBigDecimal(2, costumeImportingBill.getImportPrice());
            ps.setInt(3, costumeImportingBill.getQuantity());
            ps.setString(4, costumeImportingBill.getName());
            ps.setString(5, costumeImportingBill.getDescription());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        costumeImportingBill.setId(id);
        return costumeImportingBill;
    }

    private CostumeImportingBill updateCostumeImportingBill(CostumeImportingBill costumeImportingBill) {
        String sql = "UPDATE tblCostumeImportingBill SET costume_id = ?, import_price = ?, " +
                     "quantity = ?, name = ?, description = ? WHERE id = ?";
                     
        jdbcTemplate.update(sql, 
            costumeImportingBill.getCostume() != null ? costumeImportingBill.getCostume().getId() : null,
            costumeImportingBill.getImportPrice(),
            costumeImportingBill.getQuantity(),
            costumeImportingBill.getName(),
            costumeImportingBill.getDescription(),
            costumeImportingBill.getId()
        );
        
        return costumeImportingBill;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM tblCostumeImportingBill WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
} 