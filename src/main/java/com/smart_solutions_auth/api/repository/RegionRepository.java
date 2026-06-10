package com.smart_solutions_auth.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart_solutions_auth.api.model.entity.Region;


@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    
}
