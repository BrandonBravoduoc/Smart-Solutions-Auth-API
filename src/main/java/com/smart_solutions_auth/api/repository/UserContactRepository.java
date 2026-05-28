package com.smart_solutions_auth.api.repository;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.smart_solutions_auth.api.model.UserContact;


public interface UserContactRepository extends JpaRepository<UserContact, Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    @Cacheable(value = "userContacts", key = "#email", unless = "#result == null")
    Optional<UserContact> findByUserEmail(String email);


    @Cacheable(value = "userContacts", key = "#userId", unless = "#result == null")
    Optional<UserContact> findByUserId(Long userId);

    Optional<UserContact> findByPhoneNumber(String phoneNumber);



    
    
}
