package com.smart_solutions_auth.API.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_solutions_auth.API.dto.auth.AuthDTO;
import com.smart_solutions_auth.API.dto.user.UserDTO;
import com.smart_solutions_auth.API.service.AuthService;
import com.smart_solutions_auth.API.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthDTO.Response> login(
            @RequestBody @Valid AuthDTO.LoginRequest loginRequest,
            HttpServletResponse response) {
       
        AuthDTO.Response authResponse = authService.Login(loginRequest, response);
        
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            jakarta.servlet.http.HttpServletRequest request, 
            HttpServletResponse response) {
        
        authService.refreshToken(request, response);
        return ResponseEntity.ok().body("{\"message\": \"Token renovado exitosamente\"}");
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO.Response> register(@RequestBody @Valid UserDTO.RegisterRequest dto) {
        UserDTO.Response response = userService.userRegister(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
