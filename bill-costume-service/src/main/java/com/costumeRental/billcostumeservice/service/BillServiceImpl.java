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
    private final CustomerService customerService;

    @Autowired
    public BillServiceImpl(BillDao billDao, CustomerService customerService) {
        this.billDao = billDao;
        this.customerService = customerService;
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
        // Ensure we have the latest customer data from user-service if customerId is provided
        if (bill.getCustomer() != null && bill.getCustomer().getId() != null) {
            bill.setCustomer(customerService.getCustomerById(bill.getCustomer().getId()));
        }
        return billDao.save(bill);
    }

    @Override
    public Bill updateBill(Long id, Bill bill) {
        Bill existingBill = getBillById(id);
        
        // Update customer data from user-service if customerId is provided
        if (bill.getCustomer() != null && bill.getCustomer().getId() != null) {
            existingBill.setCustomer(customerService.getCustomerById(bill.getCustomer().getId()));
        } else {
            existingBill.setCustomer(bill.getCustomer());
        }
        
        existingBill.setRentDate(bill.getRentDate());
        existingBill.setReturnDate(bill.getReturnDate());
        existingBill.setPayment(bill.getPayment());
        existingBill.setNote(bill.getNote());
        existingBill.setAddress(bill.getAddress());
        
        return billDao.save(existingBill);
    }

    @Override
    public void deleteBill(Long id) {
        getBillById(id); // Check if exists
        billDao.deleteById(id);
    }
} 