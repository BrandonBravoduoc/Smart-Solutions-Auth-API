package com.smart_solutions_auth.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart_solutions_auth.api.model.UserContact;


public interface UserContactRepository extends JpaRepository<UserContact, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<UserContact> findByUserEmail(String email);

    Optional<UserContact> findByUserId(Long userId);

    Optional<UserContact> findByPhoneNumber(String phoneNumber);



    
    
}
