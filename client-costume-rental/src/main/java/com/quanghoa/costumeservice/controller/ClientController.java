package com.quanghoa.costumeservice.controller;

import com.quanghoa.costumeservice.model.LoginRequest;
import com.quanghoa.costumeservice.model.UserResponse;
import com.quanghoa.costumeservice.model.BillRequest;
import com.quanghoa.costumeservice.model.PaymentInfo;
import com.quanghoa.costumeservice.service.CostumeService;
import com.quanghoa.costumeservice.service.UserService;
import com.quanghoa.costumeservice.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class ClientController {

    private final UserService userService;
    private final CostumeService costumeService;
    private final BillService billService;

    @Autowired
    public ClientController(UserService userService, CostumeService costumeService, BillService billService) {
        this.userService = userService;
        this.costumeService = costumeService;
        this.billService = billService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute LoginRequest loginRequest, 
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        
        UserResponse userResponse = userService.login(loginRequest);
        
        if (userResponse != null) {
            // Authentication successful
            session.setAttribute("username", userResponse.getUsername());
            session.setAttribute("userId", userResponse.getId());
            session.setAttribute("fullName", 
                    userResponse.getFullName() != null ? 
                    userResponse.getFullName().getFirstName() + " " + userResponse.getFullName().getLastName() : 
                    userResponse.getUsername());
            
            // Store the complete user data
            session.setAttribute("userData", userResponse);
            
            // Redirect based on username
            if ("customer".equalsIgnoreCase(userResponse.getUsername())) {
                return "customer_home";
            } else if ("admin".equalsIgnoreCase(userResponse.getUsername())) {
                return "admin";
            } else {
                // Default to customer view for other users
                return "customer_home";
            }
        }
        
        // Login failed
        redirectAttributes.addFlashAttribute("error", "Invalid username or password");
        return "redirect:/login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    @GetMapping("/costumes")
    public String getCostumeViewPage(Model model) {
        // Call API to get costume data
        List<Map<String, Object>> costumes = costumeService.getAllCostumes();
        
        // Add data to model
        model.addAttribute("costumes", costumes);
        model.addAttribute("costumeCount", costumes.size());
        
        return "costume_view";
    }
    
    @GetMapping("/payment_booking")
    public String getPaymentBookingPage(Model model, HttpSession session) {
        // Kiểm tra nếu người dùng đã đăng nhập
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        
        // Get user data from session and add to model
        UserResponse userData = (UserResponse) session.getAttribute("userData");
        if (userData != null) {
            model.addAttribute("user", userData);
        }
        
        return "payment_booking";
    }

    @PostMapping("/create-bill")
    public ResponseEntity<Map<String, Object>> postCreateBill(@RequestBody BillRequest billRequest) {
        // Call the bill service to create a new bill
        Map<String, Object> response = billService.createBill(billRequest);
        return ResponseEntity.ok(response);
    }
} 