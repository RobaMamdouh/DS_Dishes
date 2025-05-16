package com.example.userservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class securityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register", "/api/users/login", "/api/users/userById", "/api/users/createCompany"
                                , "api/users/allUsers", "/api/users/allCompanies", "/api/users/getUsernameById","/api/users/getUserByUsername"
                                ,"/api/users/reduce-balance","/api/users/messages/error-logs","/api/users/messages/payment-failed", "api/users/getBalance","/api/orders/sold-dishes").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults()); // Enable HTTP Basic Auth

        return http.build();
    }

}
