package com.example.userservice.Repo;
import com.example.userservice.Models.userModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface userRepo extends JpaRepository<userModel, Long>{
    userModel findByUsername(String username);
    boolean existsByUsername(String username);
}
