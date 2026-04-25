package com.smart_solutions_auth.API.service;

import org.springframework.stereotype.Service;

import com.smart_solutions_auth.API.model.UserRole;
import com.smart_solutions_auth.API.repository.UserRoleRepository;
import com.smart_solutions_auth.API.util.Validations;

import jakarta.transaction.Transactional;
import main.java.com.smart_solutions_auth.API.dto.user.UserRoleDTO;

@Service
@Transactional
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private Validations validations;
    
    public UserRoleDTO.Response createRole(UserRoleDTO.createRoleRequest dto){

        validations.checkRole(dto.nameRole());
        UserRole newRole = new UserRole();
        newRole.setNameRole(dto.nameRole().toUpperCase().trim());
        UserRole savedRole = userRoleRepository.save(newRole);
    
        return new UserRoleDTO.createRoleResponse(
            role.getId(),
            role.getNameRole(),
            "Rol creado correctamente."
        );
    }


}
