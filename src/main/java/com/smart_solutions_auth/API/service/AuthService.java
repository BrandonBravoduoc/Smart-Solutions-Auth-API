package com.smart_solutions_auth.API.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import com.smart_solutions_auth.API.dto.auth.AuthDTO;
import com.smart_solutions_auth.API.model.User;
import com.smart_solutions_auth.API.repository.UserRepository;
import com.smart_solutions_auth.API.service.jwt.JwtService;
import com.smart_solutions_auth.API.util.Validations;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service 
@Transactional
public class AuthService {

    @Autowired 
    private Validations validations;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    public AuthDTO.Response Login(AuthDTO.LoginRequest dto, HttpServletResponse response) {
        User user = validations.validateCredentials(dto.email(), dto.password());
        
        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(false) 
                .path("/")
                .maxAge(3600) 
                .sameSite("Strict")
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/auth/refresh") 
                .maxAge(604800) 
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return new AuthDTO.Response(user.getEmail(), user.getUserRole().getNameRole());
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = Arrays.stream(request.getCookies() != null ? request.getCookies() : new Cookie[0])
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("Error contactese con el soporte."));

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new RuntimeException("Su sesión expiró, inicie sesión o contactese con el soporte.");
        }

        Long userId = jwtService.extractClaim(refreshToken, claims -> claims.get("userId", Long.class));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        String newAccessToken = jwtService.generateToken(user);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(3600)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    }
}

    

