package com.costumeRental.costumeservice.dao.impl;

import com.costumeRental.costumeservice.dao.CostumeDao;
import com.costumeRental.costumeservice.dao.CostumeBillDao;
import com.costumeRental.costumeservice.model.Costume;
import com.costumeRental.costumeservice.model.CostumeBill;
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
public class CostumeBillDaoImpl implements CostumeBillDao {

    private final JdbcTemplate jdbcTemplate;
    private final CostumeDao costumeDao;
    private final RowMapper<CostumeBill> costumeBillRowMapper;

    @Autowired
    public CostumeBillDaoImpl(JdbcTemplate jdbcTemplate, @Lazy CostumeDao costumeDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.costumeDao = costumeDao;
        this.costumeBillRowMapper = (rs, rowNum) -> {
            CostumeBill costumeBill = new CostumeBill();
            costumeBill.setId(rs.getLong("id"));
            
            Long costumeId = rs.getLong("costume_id");
            if (costumeId != 0) {
                costumeDao.findById(costumeId).ifPresent(costumeBill::setCostume);
            }
            
            costumeBill.setRentPrice(rs.getBigDecimal("rent_price"));
            costumeBill.setBillId(rs.getString("bill_id"));
            costumeBill.setQuantity(rs.getInt("quantity"));
            costumeBill.setName(rs.getString("name"));
            costumeBill.setColor(rs.getString("color"));
            costumeBill.setSize(rs.getString("size"));
            return costumeBill;
        };
    }

    @Override
    public List<CostumeBill> findAll() {
        String sql = "SELECT * FROM tblCostumeBill";
        return jdbcTemplate.query(sql, costumeBillRowMapper);
    }

    @Override
    public Optional<CostumeBill> findById(Long id) {
        try {
            String sql = "SELECT * FROM tblCostumeBill WHERE id = ?";
            CostumeBill costumeBill = jdbcTemplate.queryForObject(sql, costumeBillRowMapper, id);
            return Optional.ofNullable(costumeBill);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CostumeBill> findByBillId(String billId) {
        String sql = "SELECT * FROM tblCostumeBill WHERE bill_id = ?";
        return jdbcTemplate.query(sql, costumeBillRowMapper, billId);
    }

    @Override
    public List<CostumeBill> findByCostumeId(Long costumeId) {
        String sql = "SELECT * FROM tblCostumeBill WHERE costume_id = ?";
        return jdbcTemplate.query(sql, costumeBillRowMapper, costumeId);
    }

    @Override
    public CostumeBill save(CostumeBill costumeBill) {
        if (costumeBill.getId() == null) {
            // Insert new costume bill
            return insertCostumeBill(costumeBill);
        } else {
            // Update existing costume bill
            return updateCostumeBill(costumeBill);
        }
    }

    private CostumeBill insertCostumeBill(CostumeBill costumeBill) {
        String sql = "INSERT INTO tblCostumeBill (costume_id, rent_price, bill_id, quantity, name, color, size) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
                     
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, costumeBill.getCostume() != null ? costumeBill.getCostume().getId() : null);
            ps.setBigDecimal(2, costumeBill.getRentPrice());
            ps.setString(3, costumeBill.getBillId());
            ps.setInt(4, costumeBill.getQuantity());
            ps.setString(5, costumeBill.getName());
            ps.setString(6, costumeBill.getColor());
            ps.setString(7, costumeBill.getSize());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        costumeBill.setId(id);
        return costumeBill;
    }

    private CostumeBill updateCostumeBill(CostumeBill costumeBill) {
        String sql = "UPDATE tblCostumeBill SET costume_id = ?, rent_price = ?, bill_id = ?, " +
                     "quantity = ?, name = ?, color = ?, size = ? WHERE id = ?";
                     
        jdbcTemplate.update(sql, 
            costumeBill.getCostume() != null ? costumeBill.getCostume().getId() : null,
            costumeBill.getRentPrice(),
            costumeBill.getBillId(),
            costumeBill.getQuantity(),
            costumeBill.getName(),
            costumeBill.getColor(),
            costumeBill.getSize(),
            costumeBill.getId()
        );
        
        return costumeBill;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM tblCostumeBill WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
} 