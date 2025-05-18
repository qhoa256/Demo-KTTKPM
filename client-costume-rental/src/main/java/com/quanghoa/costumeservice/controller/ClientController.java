package com.quanghoa.costumeservice.controller;

import com.quanghoa.costumeservice.model.LoginRequest;
import com.quanghoa.costumeservice.model.UserResponse;
import com.quanghoa.costumeservice.model.BillRequest;
import com.quanghoa.costumeservice.model.PaymentInfo;
import com.quanghoa.costumeservice.service.CostumeService;
import com.quanghoa.costumeservice.service.UserService;
import com.quanghoa.costumeservice.service.BillService;
import com.quanghoa.costumeservice.service.SupplierService;
import com.quanghoa.costumeservice.service.ImportBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ClientController {

    private final UserService userService;
    private final CostumeService costumeService;
    private final BillService billService;
    private final SupplierService supplierService;
    private final ImportBillService importBillService;

    @Autowired
    public ClientController(UserService userService, CostumeService costumeService, BillService billService, SupplierService supplierService, ImportBillService importBillService) {
        this.userService = userService;
        this.costumeService = costumeService;
        this.billService = billService;
        this.supplierService = supplierService;
        this.importBillService = importBillService;
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
    public String getCostumeViewPage(Model model, 
                                    @org.springframework.web.bind.annotation.RequestParam(required = false) String search,
                                    @org.springframework.web.bind.annotation.RequestParam(required = false) String rentDate,
                                    @org.springframework.web.bind.annotation.RequestParam(required = false) String returnDate,
                                    @org.springframework.web.bind.annotation.RequestParam(required = false) String category,
                                    @org.springframework.web.bind.annotation.RequestParam(required = false) String size,
                                    @org.springframework.web.bind.annotation.RequestParam(required = false) String price,
                                    @org.springframework.web.bind.annotation.RequestParam(required = false) String color) {
        
        // Gọi API để lấy dữ liệu trang phục từ costume-service
        List<Map<String, Object>> allCostumes = costumeService.getAllCostumes(search, category, size, price, color);
        List<Map<String, Object>> availableCostumes = allCostumes;
        
        // Kiểm tra khả dụng nếu người dùng chọn khoảng thời gian thuê
        if (rentDate != null && !rentDate.isEmpty() && returnDate != null && !returnDate.isEmpty()) {
            try {
                // Gọi API bill-costume-service để lấy danh sách các trang phục đang được thuê trong khoảng thời gian
                List<Map<String, Object>> rentedCostumes = billService.getRentedCostumes(rentDate, returnDate);
                
                // Trang phục khả dụng bao gồm:
                // 1. Trang phục nằm trong bill nhưng không thuê trong khoảng thời gian đã chọn
                if (rentedCostumes != null && !rentedCostumes.isEmpty()) {
                    // Tạo set chứa ID của tất cả trang phục đang được thuê trong khoảng thời gian
                    Set<Long> rentedCostumeIds = rentedCostumes.stream()
                        .map(rentedCostume -> Long.valueOf(rentedCostume.get("costumeId").toString()))
                        .collect(Collectors.toSet());
                    
                // 2. Trang phục không nằm trong bill nào
                    availableCostumes = allCostumes.stream()
                        .filter(costume -> {
                            Long costumeId = Long.valueOf(costume.get("id").toString());
                            return !rentedCostumeIds.contains(costumeId);
                        })
                        .collect(Collectors.toList());
                    
                    // Thông báo số lượng trang phục không khả dụng
                    int unavailableCount = allCostumes.size() - availableCostumes.size();
                    if (unavailableCount > 0) {
                        model.addAttribute("unavailableCount", unavailableCount);
                    }
                }
                // Nếu không có trang phục nào đang được thuê, tất cả trang phục đều khả dụng
                // availableCostumes đã được gán = allCostumes ở trên
                
                // Thêm thông tin ngày thuê/trả vào model để hiển thị trong form tìm kiếm
                model.addAttribute("rentDate", rentDate);
                model.addAttribute("returnDate", returnDate);
                
            } catch (Exception e) {
                // Log lỗi và hiển thị thông báo cho người dùng
                System.err.println("Lỗi khi kiểm tra khả dụng trang phục: " + e.getMessage());
                model.addAttribute("errorMessage", "Không thể kiểm tra khả dụng trang phục. Vui lòng thử lại sau.");
            }
        }
        
        // Thêm các thông tin tìm kiếm và bộ lọc vào model
        model.addAttribute("search", search);
        model.addAttribute("category", category);
        model.addAttribute("size", size);
        model.addAttribute("price", price);
        model.addAttribute("color", color);
        
        // Thêm danh sách trang phục khả dụng vào model
        model.addAttribute("costumes", availableCostumes);
        model.addAttribute("costumeCount", availableCostumes.size());
        model.addAttribute("totalCount", allCostumes.size());
        
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
    public ResponseEntity<Map<String, Object>> postCreateBill(@RequestBody Map<String, Object> billRequest) {
        // Call the bill service to create a new bill
        Map<String, Object> response = billService.createBill(billRequest);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/import-costume")
    public String getImportingPage(Model model, HttpSession session) {
        // Check if user is logged in and is admin
        if (session.getAttribute("username") == null || 
            !"admin".equalsIgnoreCase((String) session.getAttribute("username"))) {
            return "redirect:/login";
        }
        
        // Get suppliers from supplier service
        List<Map<String, Object>> suppliers = supplierService.getAllSuppliers();
        
        // Add suppliers to model
        model.addAttribute("suppliers", suppliers);
        
        return "importing_costume";
    }
    
    @GetMapping("/get-costume-suppliers/{supplierId}")
    @ResponseBody
    public List<Map<String, Object>> getCostumeSuppliers(@PathVariable String supplierId) {
        // Call the external API to get costumes for this supplier
        return costumeService.getCostumesBySupplierId(supplierId);
    }

    // Add new API endpoint to fetch costumes by supplier ID
    @GetMapping("/api/costumes/supplier/{supplierId}")
    @ResponseBody
    public List<Map<String, Object>> getCostumesBySupplierId(@PathVariable String supplierId) {
        return costumeService.getCostumesBySupplierId(supplierId);
    }
    
    // New method to handle importing bill submission
    @PostMapping("/importing-bill")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> postImportingBill(@RequestBody Map<String, Object> importData, HttpSession session) {
        // Get the logged in user data from the session
        UserResponse userData = (UserResponse) session.getAttribute("userData");
        
        // Prepare the request payload for the import-bill-service
        Map<String, Object> requestPayload = importBillService.prepareImportBillRequest(userData, importData);
        
        // Call the import-bill-service to create a new importing bill
        Map<String, Object> response = importBillService.createImportBill(requestPayload);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint for displaying costume statistics and revenue reports
     */
    @GetMapping("/costume-statistic")
    public String getCostumeStatistic(Model model, HttpSession session) {
        // Check if user is logged in and is admin
        if (session.getAttribute("username") == null || 
            !"admin".equalsIgnoreCase((String) session.getAttribute("username"))) {
            return "redirect:/login";
        }
        
        // Get revenue statistics by costume category
        Map<String, Object> revenueByCategory = costumeService.getRevenueByCategory();
        
        // Add data to model
        model.addAttribute("revenueByCategory", revenueByCategory);
        
        return "costume_statistic";
    }
} 