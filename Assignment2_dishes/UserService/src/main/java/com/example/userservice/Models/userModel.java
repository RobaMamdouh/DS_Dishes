package com.example.userservice.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class userModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public roles getRole() {
        return role;
    }

    private String username;
    private String password;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    private double balance;
    @Enumerated(EnumType.STRING)
    private roles role;

    public userModel(String name, String password, roles role) {
        this.username = name;
        this.password = password;
        this.role = role;
    }

    public userModel() {

    }

    public void setUsername(String name) {
        this.username = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(roles role) {
        this.role = role;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
}
