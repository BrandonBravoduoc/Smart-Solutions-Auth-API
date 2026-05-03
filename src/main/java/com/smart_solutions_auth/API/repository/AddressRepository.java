package com.smart_solutions_auth.API.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart_solutions_auth.API.model.Address;


@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    
}
