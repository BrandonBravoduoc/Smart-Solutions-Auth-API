package com.smart_solutions_auth.API.service;

import java.util.List;

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
    
    public UserRoleDTO.Response createRole(UserRoleDTO.CreateRoleRequest dto){

        validations.checkRole(dto.nameRole());
        UserRole newRole = new UserRole();
        newRole.setNameRole(dto.nameRole().toUpperCase().trim());
        UserRole savedRole = userRoleRepository.save(newRole);
    
        return new UserRoleDTO.Response(
            savedRole.getId(),
            savedRole.getNameRole(),
            "Rol creado correctamente."
        );
    }

    public UserRoleDTO.Response updateRole(Long id, String nameRole) {
        UserRole role = userRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado."));

        if (nameRole != null && !nameRole.isBlank()) {
            role.setNameRole(nameRole.trim().toUpperCase()); 
            userRoleRepository.save(role); 
        } else {
            throw new RuntimeException("El nombre del rol no puede estar vacío.");
        }

        return new UserRoleDTO.Response(
                role.getId(),
                role.getNameRole(),
                "Rol actualizado correctamente."
        );
    }

    public List<UserRoleDTO.Response> listRoles(){
        return userRoleRepository.findAll().stream()
            .map(userRole -> new UserRoleDTO.Response(
                userRole.getId(),
                userRole.getNameRole(), 
                null))
            .toList();
    }

    public void deleteRolByName(String nameRole){
        UserRole role = userRoleRepository.findByNameRole(nameRole.toUpperCase().trim())
            .orElseThrow(() -> new RuntimeException("El rol "+ nameRole + " no fue encontrado."));
        
        userRoleRepository.delete(role);
    }



}
