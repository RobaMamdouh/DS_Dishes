package com.example.userservice.Controllers;

import com.example.userservice.Models.roles;
import com.example.userservice.Models.userModel;
import com.example.userservice.Services.ResponseMessage;
import com.example.userservice.Services.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class userController {
    @Autowired
    private userService userService;

    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody userModel user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok(new ResponseMessage("User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(@RequestParam String username, @RequestParam String password) {
        try {
            roles role = userService.login(username, password);
            switch (role) {
                case ADMIN:
                    return ResponseEntity.ok(new ResponseMessage("Logged in as admin successfully"));
                case COMPANY:
                    return ResponseEntity.ok(new ResponseMessage("Logged in as company successfully"));
                case USER:
                    return ResponseEntity.ok(new ResponseMessage("Logged in as user successfully"));
                default:
                    return ResponseEntity.badRequest().body(new ResponseMessage("Unknown role"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }


    @PostMapping("/createCompany")
    public ResponseEntity<ResponseMessage> createCompany(@RequestBody Map<String, String> payload) {
        try {
            String username = payload.get("username");
            userService.createCompany(username);
            return ResponseEntity.ok(new ResponseMessage("Company created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: " + e.getMessage()));
        }
    }


    @GetMapping("/allUsers")
    public List<userModel> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/userById")
    public userModel getUserById(@RequestParam long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/allCompanies")
    public List<userModel> getAllCompanies() {
        return userService.getAllCompanies();
    }

    @GetMapping("/getUsernameById")
    public String getUsernameById(@RequestParam long userId) {
        return userService.getUsernameById(userId);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/getUserByUsername")
    public userModel getUserByUsername(@RequestParam String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/reduce-balance")
    public ResponseEntity<?> reduceBalance(@RequestParam long userId, @RequestParam double amount) {
        boolean success = userService.reduceBalance(userId, amount);
        if (!success) {
            return ResponseEntity.badRequest().body("Insufficient balance.");
        }
        return ResponseEntity.ok("Balance reduced successfully.");
    }

    @GetMapping("/getBalance")
    public ResponseEntity<?> getBalance(@RequestParam long userId) {
        try {
            double balance = userService.getBalance(userId);
            return ResponseEntity.ok(balance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }


    @GetMapping("/messages/payment-failed")
    public List<String> getPaymentFailedMessages() {
        return userService.getPaymentFailedMessages();
    }

    @GetMapping("/messages/error-logs")
    public List<String> getErrorLogMessages() {
        return userService.getErrorLogMessages();
    }

}
