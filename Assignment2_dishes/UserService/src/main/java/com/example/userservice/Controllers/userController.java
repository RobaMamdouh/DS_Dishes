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

    //    @PostMapping("/register")
//    public String registerUser(@RequestBody userModel user) {
//        try {
//            userService.registerUser(user);
//            return "User registered successfully";
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }
    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody userModel user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok(new ResponseMessage("User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: " + e.getMessage()));
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
//    @PostMapping("/login")
//    public String login(@RequestParam String username, @RequestParam String password) {
//        try {
//            roles role = userService.login(username, password);
//            switch (role) {
//                case ADMIN:
//                    return "Logged in as admin successfully";
//                case COMPANY:
//                    return "Logged in as company successfully";
//                case USER:
//                    return "Logged in as user successfully";
//                default:
//                    return "Unknown role";
//            }
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }

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


//    @PostMapping("/createCompany")
//    public String createCompany(@RequestParam String username) {
//        try {
//            userService.createCompany(username);
//            return "Company created successfully";
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }

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
}
