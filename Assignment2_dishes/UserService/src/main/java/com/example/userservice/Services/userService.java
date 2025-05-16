package com.example.userservice.Services;

import com.example.userservice.Models.roles;
import com.example.userservice.Models.userModel;
import com.example.userservice.Repo.userRepo;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class userService {
    @Autowired
    private userRepo userRepo;


    public void registerUser(userModel user) {
        if (userRepo.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User already exists");
        }
        userRepo.save(user);
        System.out.println("User registered successfully: " + user.getUsername() + ", Password: " + user.getPassword());
    }

//    public void loginAdmin(String username, String password) {
//        userModel user = validateUser(username, password);
//        if (user.getRole() != roles.ADMIN) {
//            throw new RuntimeException("Not an admin account");
//        }
//    }
//
//    public void loginCompany(String username, String password) {
//        userModel user = validateUser(username, password);
//        if (user.getRole() != roles.COMPANY) {
//            throw new RuntimeException("Not a company account");
//        }
//    }
//
//    public void loginUser(String username, String password) {
//        userModel user = validateUser(username, password);
//        if (user.getRole() != roles.USER) {
//            throw new RuntimeException("Not a regular user account");
//        }
//    }

    public roles login(String username, String password) {
        userModel user = validateUser(username, password);
        return user.getRole();
    }


    private userModel validateUser(String username, String password) {
        userModel user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    private String generatePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public void createCompany(String username){
        if(userRepo.existsByUsername(username)){
            throw new RuntimeException("Company already exists");
        }
        String password = generatePassword(8);
        userModel company = new userModel();
        company.setUsername(username);
        company.setPassword(password);
        company.setRole(roles.COMPANY);
        System.out.println("Company password for " + username + " is: " + password);
        userRepo.save(company);
    }

    public List<userModel> getAllUsers() {
        return userRepo.findAll().stream()
                .filter(user -> user.getRole() == roles.USER)
                .toList();
    }

    public userModel getUserById(long id) {
        return userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<userModel> getAllCompanies() {
        return userRepo.findAll().stream()
                .filter(user -> user.getRole() == roles.COMPANY)
                .toList();
    }

    public String getUsernameById(long id) {
        userModel user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getUsername();
    }

    public userModel getUserByUsername(String username) {
        userModel user = userRepo.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    public boolean reduceBalance(long userId, double amount) {
        userModel user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getBalance() < amount) {
            return false;
        }

        user.setBalance(user.getBalance() - amount);
        userRepo.save(user);
        return true;
    }

    public double getBalance(long userId) {
        userModel user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getBalance();
    }


    private final List<String> paymentFailedMessages = new ArrayList<>();
    private final List<String> errorLogMessages = new ArrayList<>();

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "admin.payment.failed", durable = "true"),
            exchange = @Exchange(value = "payments", type = "direct"),
            key = "PaymentFailed"
    ))
    public void handlePaymentFailed(String message) {
        String logMessage = "Admin Alert: " + message;
        System.out.println(logMessage);
        paymentFailedMessages.add(logMessage);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "admin.error.logs", durable = "true"),
            exchange = @Exchange(value = "log", type = "topic"),
            key = "*.ERROR"
    ))
    public void handleErrorLogs(String message) {
        String logMessage = "Admin Log Alert: " + message;
        System.out.println(logMessage);
        errorLogMessages.add(logMessage);
    }

    public List<String> getPaymentFailedMessages() {
        return paymentFailedMessages;
    }

    public List<String> getErrorLogMessages() {
        return errorLogMessages;
    }

}
