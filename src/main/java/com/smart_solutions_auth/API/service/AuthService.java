package com.smart_solutions_auth.API.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smart_solutions_auth.API.dto.auth.AuthDTO;
import com.smart_solutions_auth.API.model.User;
import com.smart_solutions_auth.API.service.jwt.JwtService;
import com.smart_solutions_auth.API.util.Validations;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service 
@Transactional
public class AuthService {


    @Autowired 
    private Validations validations;

    @Autowired
    private JwtService jwtService;

    public AuthDTO.Response Login(AuthDTO.LoginRequest dto, HttpServletResponse response) {
        
        User user = validations.validateCredentials(dto.email(), dto.password());
        
        String token = jwtService.generateToken(user);

        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);

        response.addCookie(cookie);

        return new AuthDTO.Response(
            user.getEmail(),
            user.getUserRole().getNameRole()
        );
    }

    
}
