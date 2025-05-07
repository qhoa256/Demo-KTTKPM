package com.costumeRental.costumeservice.dao.impl;

import com.costumeRental.costumeservice.dao.CostumeDao;
import com.costumeRental.costumeservice.model.Costume;
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
public class CostumeDaoImpl implements CostumeDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Costume> costumeRowMapper = (rs, rowNum) -> {
        Costume costume = new Costume();
        costume.setId(rs.getLong("id"));
        costume.setCategory(rs.getString("category"));
        costume.setName(rs.getString("name"));
        costume.setDescription(rs.getString("description"));
        costume.setPrice(rs.getBigDecimal("price"));
        return costume;
    };

    @Autowired
    public CostumeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Costume> findAll() {
        String sql = "SELECT * FROM tblCostume";
        return jdbcTemplate.query(sql, costumeRowMapper);
    }

    @Override
    public Optional<Costume> findById(Long id) {
        try {
            String sql = "SELECT * FROM tblCostume WHERE id = ?";
            Costume costume = jdbcTemplate.queryForObject(sql, costumeRowMapper, id);
            return Optional.ofNullable(costume);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Costume> findByCategory(String category) {
        String sql = "SELECT * FROM tblCostume WHERE category = ?";
        return jdbcTemplate.query(sql, costumeRowMapper, category);
    }

    @Override
    public Costume save(Costume costume) {
        if (costume.getId() == null) {
            // Insert new costume
            return insertCostume(costume);
        } else {
            // Update existing costume
            return updateCostume(costume);
        }
    }

    private Costume insertCostume(Costume costume) {
        String sql = "INSERT INTO tblCostume (category, name, description, price) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, costume.getCategory());
            ps.setString(2, costume.getName());
            ps.setString(3, costume.getDescription());
            ps.setBigDecimal(4, costume.getPrice());
            return ps;
        }, keyHolder);

        Long id = keyHolder.getKey().longValue();
        costume.setId(id);
        return costume;
    }

    private Costume updateCostume(Costume costume) {
        String sql = "UPDATE tblCostume SET category = ?, name = ?, description = ?, price = ? WHERE id = ?";
        jdbcTemplate.update(sql, 
            costume.getCategory(), 
            costume.getName(),
            costume.getDescription(),
            costume.getPrice(),
            costume.getId()
        );
        return costume;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM tblCostume WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
} 