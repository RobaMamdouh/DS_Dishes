package com.example.userservice.Controllers;

import com.example.userservice.Models.roles;
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

//    @PostMapping("/loginUser")
//    public String loginUser(@RequestParam String username, @RequestParam String password) {
//        try {
//            userService.loginUser(username, password);
//            return "Logged in as a user successfully";
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }
//
//    @PostMapping("/loginAdmin")
//    public String loginAdmin(@RequestParam String username, @RequestParam String password) {
//        try {
//            userService.loginAdmin(username, password);
//            return "Logged in as an admin successfully";
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }
//
//    @PostMapping("/loginCompany")
//    public String loginCompany(@RequestParam String username, @RequestParam String password) {
//        try {
//            userService.loginCompany(username, password);
//            return "Logged in as a company successfully";
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        try {
            roles role = userService.login(username, password);
            switch (role) {
                case ADMIN:
                    return "Logged in as admin successfully";
                case COMPANY:
                    return "Logged in as company successfully";
                case USER:
                    return "Logged in as user successfully";
                default:
                    return "Unknown role";
            }
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

        @GetMapping("/getUsernameById")
    public String getUsernameById(@RequestParam long userId) {
        return userService.getUsernameById(userId);
    }
}
