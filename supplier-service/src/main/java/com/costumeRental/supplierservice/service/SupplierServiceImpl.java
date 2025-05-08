package com.costumeRental.supplierservice.service;

import com.costumeRental.supplierservice.dao.SupplierDao;
import com.costumeRental.supplierservice.model.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {
    private final SupplierDao supplierDao;

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierDao.findAll();
    }

    @Override
    public Supplier getSupplierById(Long id) {
        return supplierDao.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
    }

    @Override
    public Supplier createSupplier(Supplier supplier) {
        return supplierDao.save(supplier);
    }

    @Override
    public Supplier updateSupplier(Long id, Supplier supplier) {
        Supplier existingSupplier = getSupplierById(id);
        
        existingSupplier.setName(supplier.getName());
        existingSupplier.setEmail(supplier.getEmail());
        existingSupplier.setContact(supplier.getContact());
        existingSupplier.setAddress(supplier.getAddress());
        
        return supplierDao.save(existingSupplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        Supplier supplier = getSupplierById(id);
        supplierDao.delete(supplier);
    }
} 