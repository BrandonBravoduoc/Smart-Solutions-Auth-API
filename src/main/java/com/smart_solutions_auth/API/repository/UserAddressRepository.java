package com.smart_solutions_auth.API.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart_solutions_auth.API.model.UserAddress;


@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {
    
}
