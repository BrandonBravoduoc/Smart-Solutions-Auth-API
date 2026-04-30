package com.smart_solutions_auth.API.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_solutions_auth.API.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;


    @PostMapping("/logout") 
    public ResponseEntity<?> logout(HttpServletResponse response) {
        userService.logout(response); 
        return ResponseEntity.ok().body("{\"message\": \"Sesión cerrada exitosamente\"}");
    }
    
}
