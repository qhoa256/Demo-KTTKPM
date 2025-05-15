package com.costumeRental.importbillservice.dao;

import com.costumeRental.importbillservice.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ImportingBillDaoImpl implements ImportingBillDao {

    private final DataSource dataSource;
    private final RestTemplate restTemplate;
    private JdbcTemplate jdbcTemplate;

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${costume.service.url}")
    private String costumeServiceUrl;

    private JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        return jdbcTemplate;
    }

    private final RowMapper<ImportingBill> importingBillRowMapper = (rs, rowNum) -> {
        ImportingBill importingBill = new ImportingBill();
        importingBill.setId(rs.getLong("id"));

        // Create Staff object
        Staff manager = new Staff();
        manager.setId(rs.getLong("managerId"));
        importingBill.setManager(manager);

        // Create Supplier object
        Supplier supplier = new Supplier();
        supplier.setId(rs.getLong("supplierId"));
        importingBill.setSupplier(supplier);

        Date importDate = rs.getDate("importDate");
        if (importDate != null) {
            importingBill.setImportDate(importDate.toLocalDate());
        }
        return importingBill;
    };

    private final RowMapper<Costume> costumeRowMapper = (rs, rowNum) -> {
        Costume costume = new Costume();
        costume.setId(rs.getLong("costume_id"));
        costume.setName(rs.getString("costume_name"));
        costume.setCategory(rs.getString("category"));
        costume.setDescription(rs.getString("costume_description"));
        costume.setPrice(rs.getBigDecimal("price"));
        return costume;
    };

    private final RowMapper<Supplier> supplierRowMapper = (rs, rowNum) -> {
        Supplier supplier = new Supplier();
        supplier.setId(rs.getLong("supplier_id"));
        supplier.setName(rs.getString("supplier_name"));
        supplier.setEmail(rs.getString("email"));
        supplier.setContact(rs.getString("contact"));
        supplier.setAddress(rs.getString("address"));
        return supplier;
    };

    private final RowMapper<CostumeSupplier> costumeSupplierRowMapper = (rs, rowNum) -> {
        CostumeSupplier costumeSupplier = new CostumeSupplier();
        costumeSupplier.setId(rs.getLong("costumeSupplier_id"));

        Costume costume = new Costume();
        costume.setId(rs.getLong("costume_id"));
        costume.setName(rs.getString("costume_name"));
        costume.setCategory(rs.getString("category"));
        costume.setDescription(rs.getString("costume_description"));
        costume.setPrice(rs.getBigDecimal("price"));
        costumeSupplier.setCostume(costume);

        Supplier supplier = new Supplier();
        supplier.setId(rs.getLong("supplier_id"));
        supplier.setName(rs.getString("supplier_name"));
        supplier.setEmail(rs.getString("email"));
        supplier.setContact(rs.getString("contact"));
        supplier.setAddress(rs.getString("address"));
        costumeSupplier.setSupplier(supplier);

        return costumeSupplier;
    };

    private final RowMapper<CostumeImportingBill> costumeImportingBillRowMapper = (rs, rowNum) -> {
        CostumeImportingBill costumeImportingBill = new CostumeImportingBill();
        costumeImportingBill.setId(rs.getLong("id"));
        costumeImportingBill.setName(rs.getString("name"));
        costumeImportingBill.setDescription(rs.getString("description"));
        costumeImportingBill.setImportPrice(rs.getBigDecimal("importPrice"));
        costumeImportingBill.setQuantity(rs.getInt("quantity"));

        // Map costumeSupplier data if available
        try {
            Long costumeSupplierID = rs.getLong("costumeSupplierID");
            if (costumeSupplierID > 0 && !rs.wasNull()) {
                CostumeSupplier costumeSupplier = new CostumeSupplier();
                costumeSupplier.setId(costumeSupplierID);

                Costume costume = new Costume();
                costume.setId(rs.getLong("costume_id"));
                costume.setName(rs.getString("costume_name"));
                costume.setCategory(rs.getString("category"));
                costume.setDescription(rs.getString("costume_description"));
                costume.setPrice(rs.getBigDecimal("price"));
                costumeSupplier.setCostume(costume);

                Supplier supplier = new Supplier();
                supplier.setId(rs.getLong("supplier_id"));
                supplier.setName(rs.getString("supplier_name"));
                supplier.setEmail(rs.getString("email"));
                supplier.setContact(rs.getString("contact"));
                supplier.setAddress(rs.getString("address"));
                costumeSupplier.setSupplier(supplier);

                costumeImportingBill.setCostumeSupplier(costumeSupplier);
            }
        } catch (Exception e) {
            // CostumeSupplier data not available in this result set
        }

        return costumeImportingBill;
    };

    private final RowMapper<Staff> staffRowMapper = (rs, rowNum) -> {
        Staff staff = new Staff();
        staff.setId(rs.getLong("staff_id"));
        staff.setUsername(rs.getString("username"));
        staff.setPassword(rs.getString("password"));
        staff.setPosition(rs.getString("position"));

        try {
            // Map Address
            Long addressId = rs.getLong("address_id");
            if (addressId > 0 && !rs.wasNull()) {
                Address address = new Address();
                address.setId(addressId);
                address.setStreet(rs.getString("street"));
                address.setCity(rs.getString("city"));
                address.setUser(staff);
                staff.setAddress(address);
            }

            // Map FullName
            Long fullNameId = rs.getLong("fullname_id");
            if (fullNameId > 0 && !rs.wasNull()) {
                FullName fullName = new FullName();
                fullName.setId(fullNameId);
                fullName.setFirstName(rs.getString("firstName"));
                fullName.setLastName(rs.getString("lastName"));
                fullName.setUser(staff);
                staff.setFullName(fullName);
            }
        } catch (Exception e) {
            // Address or FullName data not available
        }

        return staff;
    };

    @Override
    public List<ImportingBill> findAll() {
        String sql = "SELECT ib.*" +
                "FROM tblImportingBill ib ";
        List<ImportingBill> importingBills = getJdbcTemplate().query(sql, importingBillRowMapper);

        // Fetch staff and costume importing bills for each importing bill
        for (ImportingBill importingBill : importingBills) {
            // First load costume importing bills
            importingBill.setCostumeImportingBills(findCostumeImportingBillsByImportingBillId(importingBill.getId()));

            // Then load staff and supplier details
            loadStaffDetails(importingBill);
            loadSupplierDetails(importingBill);
        }

        return importingBills;
    }

    @Override
    public Optional<ImportingBill> findById(Long id) {
        try {
            // Get the basic importing bill data from the database
            String sql = "SELECT ib.*" +
                    "FROM tblImportingBill ib " +
                    "WHERE ib.id = ?";
            ImportingBill importingBill = getJdbcTemplate().queryForObject(sql, importingBillRowMapper, id);

            if (importingBill != null) {
                // Get costume importing bills using the existing method
                importingBill.setCostumeImportingBills(findCostumeImportingBillsByImportingBillId(id));

                // Then load staff and supplier details
                loadStaffDetails(importingBill);
                loadSupplierDetails(importingBill);
            }

            return Optional.ofNullable(importingBill);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private void loadStaffDetails(ImportingBill importingBill) {
        if (importingBill.getManager() != null && importingBill.getManager().getId() != null) {
            try {
                // Call user-service API to get staff details
                String staffApiUrl = userServiceUrl + "/api/staff/" + importingBill.getManager().getId();
                Staff staff = restTemplate.getForObject(staffApiUrl, Staff.class);

                if (staff != null) {
                    importingBill.setManager(staff);
                }
            } catch (Exception e) {
                // Log error and continue with minimal staff information
                System.err.println("Error fetching staff details from user-service: " + e.getMessage());
            }
        }
    }

    private void loadSupplierDetails(ImportingBill importingBill) {
        // Get supplier from the first costume importing bill
        if (importingBill.getCostumeImportingBills() != null && !importingBill.getCostumeImportingBills().isEmpty()) {
            CostumeImportingBill firstBill = importingBill.getCostumeImportingBills().get(0);
            if (firstBill.getCostumeSupplier() != null && firstBill.getCostumeSupplier().getSupplier() != null) {
                importingBill.setSupplier(firstBill.getCostumeSupplier().getSupplier());
            }
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
            ps.setLong(1, importingBill.getManager().getId());
            ps.setLong(2, importingBill.getSupplier().getId());
            ps.setDate(3, importingBill.getImportDate() != null ?
                    Date.valueOf(importingBill.getImportDate()) : null);
            return ps;
        }, keyHolder);

        importingBill.setId(keyHolder.getKey().longValue());

        // Save costume importing bills if any
        if (importingBill.getCostumeImportingBills() != null && !importingBill.getCostumeImportingBills().isEmpty()) {
            for (CostumeImportingBill costumeImportingBill : importingBill.getCostumeImportingBills()) {
                saveCostumeImportingBill(importingBill.getId(), costumeImportingBill);
            }
        }

        return importingBill;
    }

    private ImportingBill update(ImportingBill importingBill) {
        String sql = "UPDATE tblImportingBill SET managerId = ?, supplierId = ?, importDate = ? WHERE id = ?";
        getJdbcTemplate().update(sql,
                importingBill.getManager().getId(),
                importingBill.getSupplier().getId(),
                importingBill.getImportDate() != null ? Date.valueOf(importingBill.getImportDate()) : null,
                importingBill.getId());

        // Update costume importing bills if any
        if (importingBill.getCostumeImportingBills() != null) {
            for (CostumeImportingBill costumeImportingBill : importingBill.getCostumeImportingBills()) {
                saveCostumeImportingBill(importingBill.getId(), costumeImportingBill);
            }
        }

        return importingBill;
    }

    @Override
    public void delete(ImportingBill importingBill) {
        // First delete all associated costume importing bills
        String deleteDetailsSql = "DELETE FROM tblCostumeImportingBill WHERE importingBillId = ?";
        getJdbcTemplate().update(deleteDetailsSql, importingBill.getId());

        // Then delete the importing bill
        String sql = "DELETE FROM tblImportingBill WHERE id = ?";
        getJdbcTemplate().update(sql, importingBill.getId());
    }

    @Override
    public List<CostumeImportingBill> findCostumeImportingBillsByImportingBillId(Long importingBillId) {
        try {
            String apiUrl = costumeServiceUrl + "/api/costume-importing-bills/importing-bill/" + importingBillId;
            CostumeImportingBill[] response = restTemplate.getForObject(apiUrl, CostumeImportingBill[].class);
            return response != null ? Arrays.asList(response) : new ArrayList<>();
        } catch (Exception e) {
            // Log error and return empty list
            System.err.println("Error fetching costume importing bills from costume-service: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public CostumeImportingBill saveCostumeImportingBill(Long importingBillId, CostumeImportingBill costumeImportingBill) {
        if (costumeImportingBill.getId() == null) {
            return insertCostumeImportingBill(importingBillId, costumeImportingBill);
        } else {
            return updateCostumeImportingBill(importingBillId, costumeImportingBill);
        }
    }

    private CostumeImportingBill insertCostumeImportingBill(Long importingBillId, CostumeImportingBill costumeImportingBill) {
        try {
            // Call the costume-service API endpoint instead of directly inserting to database
            String apiUrl = costumeServiceUrl + "/api/costume-importing-bills/importing-bill/" + importingBillId;
            
            // Make a POST request to the costume-service and handle ResponseEntity wrapper
            ResponseEntity<CostumeImportingBill> response = restTemplate.postForEntity(
                apiUrl,
                costumeImportingBill,
                CostumeImportingBill.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to create costume importing bill. Status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error calling costume-service: " + e.getMessage());
            throw new RuntimeException("Failed to create costume importing bill", e);
        }
    }

    private CostumeImportingBill updateCostumeImportingBill(Long importingBillId, CostumeImportingBill costumeImportingBill) {
        // Call the costume-service API endpoint to update the costume importing bill
        String apiUrl = costumeServiceUrl + "/api/costume-importing-bills/" + costumeImportingBill.getId();
        
        // Make a PUT request to the costume-service
        restTemplate.put(apiUrl, costumeImportingBill);
        
        // GET the updated entity
        return restTemplate.getForObject(apiUrl, CostumeImportingBill.class);
    }

    @Override
    public void deleteCostumeImportingBill(Long id) {
        // Call the costume-service API endpoint to delete the costume importing bill
        String apiUrl = costumeServiceUrl + "/api/costume-importing-bills/" + id;
        restTemplate.delete(apiUrl);
    }
}