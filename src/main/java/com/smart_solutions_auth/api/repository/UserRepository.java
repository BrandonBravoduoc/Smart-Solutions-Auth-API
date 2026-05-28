package com.smart_solutions_auth.api.repository;


import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smart_solutions_auth.api.model.User;




public interface UserRepository extends JpaRepository<User, Long> {
    
    @Cacheable(value = "users", key = "#email", unless = "#result == null")
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    
}
