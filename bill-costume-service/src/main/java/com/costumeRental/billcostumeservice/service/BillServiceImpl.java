package com.costumeRental.billcostumeservice.service;

import com.costumeRental.billcostumeservice.exception.ResourceNotFoundException;
import com.costumeRental.billcostumeservice.model.Bill;
import com.costumeRental.billcostumeservice.dao.BillDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillServiceImpl implements BillService {
    private final BillDao billDao;

    @Autowired
    public BillServiceImpl(BillDao billDao) {
        this.billDao = billDao;
    }

    @Override
    public List<Bill> getAllBills() {
        return billDao.findAll();
    }

    @Override
    public Bill getBillById(Long id) {
        return billDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + id));
    }

    @Override
    public Bill createBill(Bill bill) {
        return billDao.save(bill);
    }

    @Override
    public Bill updateBill(Long id, Bill bill) {
        Bill existingBill = getBillById(id);
        
        existingBill.setCustomerId(bill.getCustomerId());
        existingBill.setRentDate(bill.getRentDate());
        existingBill.setReturnDate(bill.getReturnDate());
        existingBill.setPayment(bill.getPayment());
        
        return billDao.save(existingBill);
    }

    @Override
    public void deleteBill(Long id) {
        getBillById(id); // Check if exists
        billDao.deleteById(id);
    }
} 