package com.smart_solutions_auth.API.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart_solutions_auth.API.dto.user.UserRoleDTO;
import com.smart_solutions_auth.API.model.UserRole;
import com.smart_solutions_auth.API.repository.UserRoleRepository;
import com.smart_solutions_auth.API.util.Validations;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private Validations validations;
    
    public UserRoleDTO.CreateRoleResponse createRole(UserRoleDTO.CreateRoleRequest dto){

        validations.checkRole(dto.nameRole());
        UserRole newRole = new UserRole();
        newRole.setNameRole(dto.nameRole().toUpperCase().trim());
        UserRole savedRole = userRoleRepository.save(newRole);
    
        return new UserRoleDTO.CreateRoleResponse(
            savedRole.getId(),
            savedRole.getNameRole(),
            "Rol creado correctamente."
        );
    }


}
