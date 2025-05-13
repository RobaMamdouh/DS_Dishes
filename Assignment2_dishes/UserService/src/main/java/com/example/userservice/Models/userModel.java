package com.example.userservice.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;

import jakarta.persistence.*;

@Entity
public class userModel
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private roles role;

    public userModel(String name, String password, roles role) {
        this.username = name;
        this.password = password;
        this.role = role;
    }

    public userModel() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public roles getRole() {
        return role;
    }

    public void setRole(roles role) {
        this.role = role;
    }
}
