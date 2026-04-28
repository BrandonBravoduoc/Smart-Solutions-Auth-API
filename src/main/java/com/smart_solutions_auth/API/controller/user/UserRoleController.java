package com.smart_solutions_auth.API.controller.user;

import org.springframework.web.bind.annotation.RestController;

import com.smart_solutions_auth.API.dto.user.UserRoleDTO;
import com.smart_solutions_auth.API.service.user.UserRoleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping
    public ResponseEntity<UserRoleDTO.CreateRoleResponse> createRole(@RequestBody @Valid UserRoleDTO.CreateRoleRequest dto) {
        UserRoleDTO.CreateRoleResponse response = userRoleService.createRole(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
