package com.smart_solutions_auth.API.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart_solutions_auth.API.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    
}
