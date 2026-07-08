package com.smart_solutions_auth.api.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.smart_solutions_auth.api.dto.auth.AuthDTO;
import com.smart_solutions_auth.api.model.entity.User;
import com.smart_solutions_auth.api.repository.UserRepository;
import com.smart_solutions_auth.api.service.jwt.JwtService;
import com.smart_solutions_auth.api.util.Validations;

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

    @Value("${cookie.secure:true}")
    private boolean cookieSecure;

    @Value("${cookie.same-site:None}")
    private String cookieSameSite;

    public AuthDTO.Response Login(AuthDTO.LoginRequest dto, HttpServletResponse response) {
        User user = validations.validateCredentials(dto.email(), dto.password());

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(3600)
                .sameSite(cookieSameSite)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/api/v1/auth/refresh")
                .maxAge(604800)
                .sameSite(cookieSameSite)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return new AuthDTO.Response(user.getEmail(), user.getUserRole().getNameRole());
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> "refreshToken".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new RuntimeException("No hay refresh token"));

        Long userId = jwtService.extractClaim(refreshToken, claims -> claims.get("userId", Long.class));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new RuntimeException("Refresh token inválido o expirado.");
        }

        String newAccessToken = jwtService.generateToken(user);
        ResponseCookie cookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(cookieSecure)
                .path("/")
                .maxAge(3600)
                .sameSite(cookieSameSite)
                .build();
        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
    

