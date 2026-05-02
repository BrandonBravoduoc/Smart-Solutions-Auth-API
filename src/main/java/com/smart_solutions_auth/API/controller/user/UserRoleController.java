package com.smart_solutions_auth.API.controller.user;

import com.smart_solutions_auth.API.dto.user.UserRoleDTO;
import com.smart_solutions_auth.API.service.UserRoleService; 
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping("/create")
    public ResponseEntity<UserRoleDTO.Response> createRole(@RequestBody @Valid UserRoleDTO.CreateRoleRequest dto) {
        UserRoleDTO.Response response = userRoleService.createRole(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserRoleDTO.Response>> listRoles() {
        List<UserRoleDTO.Response> roles = userRoleService.listRoles();
        return ResponseEntity.ok(roles);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserRoleDTO.Response> updateRole(
            @PathVariable Long id, 
            @RequestBody String nameRole
    ) {
        return ResponseEntity.ok(userRoleService.updateRole(id, nameRole));
    }

    @DeleteMapping("/{nameRole}")
    public ResponseEntity<Void> deleteRole(@PathVariable String nameRole) {
        userRoleService.deleteRolByName(nameRole);
        return ResponseEntity.noContent().build();
    }


}
