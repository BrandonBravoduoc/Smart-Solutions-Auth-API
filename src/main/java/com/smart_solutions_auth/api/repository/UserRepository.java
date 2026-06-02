package com.smart_solutions_auth.api.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart_solutions_auth.api.model.entity.User;


public interface UserRepository extends JpaRepository<User, Long> {
    
    
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    
}
