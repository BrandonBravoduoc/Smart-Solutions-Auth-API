package com.smart_solutions_auth.API.controller.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_solutions_auth.API.dto.user.UserDTO;
import com.smart_solutions_auth.API.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasRole('ADMINISTRADOR')")
@RequiredArgsConstructor
public class AdminUserController {
    
    private final UserService userService;


    @GetMapping("/search-email/{email}")
    public ResponseEntity<UserDTO.Response> searchByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/search-phone/{phone}")
    public ResponseEntity<UserDTO.Response> searchByPhone(@PathVariable String phone) {
        return ResponseEntity.ok(userService.getUserByPhone(phone));
    }


    @PatchMapping("/update-by-email/{email}")
    public ResponseEntity<UserDTO.Response> updateEmail(
            @PathVariable String email,
            @Valid @RequestBody UserDTO.UpdateUserByAdmin dto) {
        return ResponseEntity.ok(userService.updateByEmail(email, dto));
    }

    @PatchMapping("/update-by-phone/{phone}")
    public ResponseEntity<UserDTO.Response> updatePhone(
            @PathVariable String phone,
            @Valid @RequestBody UserDTO.UpdateUserByAdmin dto) {
        return ResponseEntity.ok(userService.updateByPhone(phone, dto));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO.Response>> listAllUsers() {
        List<UserDTO.Response> users = userService.listUsers();
     
        return ResponseEntity.ok(users);
    }
}
