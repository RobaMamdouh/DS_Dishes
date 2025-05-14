package com.example.userservice.Controllers;

import com.example.userservice.Models.userModel;
import com.example.userservice.Services.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class userController {
    @Autowired
    private userService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody userModel user) {
        try {
            userService.registerUser(user);
            return "User registered successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password) {
        try {
            userService.loginUser(username, password);
            return "Login successful";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    @PostMapping("/createCompany")
    public String createCompany(@RequestParam String username) {
        try {
            userService.createCompany(username);
            return "Company created successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
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
}
