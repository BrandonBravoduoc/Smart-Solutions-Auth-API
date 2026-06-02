package com.smart_solutions_auth.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smart_solutions_auth.api.model.entity.UserRole;




public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByNameRole(String nameRole);

    
}
