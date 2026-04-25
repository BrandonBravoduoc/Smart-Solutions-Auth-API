package com.smart_solutions_auth.API.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.smart_solutions_auth.API.model.User;
import com.smart_solutions_auth.API.repository.UserContactRepository;
import com.smart_solutions_auth.API.repository.UserRepository;

@Component
public class Validations {

    @Autowired private UserRepository userRepository;
    @Autowired private UserContactRepository userContactRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // Solo verificamos existencia en DB y normalizamos
    public String emailValidate(String email) {
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }
        return email.trim().toLowerCase();
    }

    // Solo verificamos la igualdad (porque el formato ya lo validó el DTO)
    public void passwordValidate(String password, String confirmPassword) {
        if(!password.equals(confirmPassword)){
            throw new RuntimeException("Las contraseñas no coinciden.");
        }
    }

    // Solo verificamos unicidad del teléfono
    public void contactValidate(String phoneNumber) {
        if(userContactRepository.existsByPhoneNumber(phoneNumber)){
            throw new RuntimeException("El número de teléfono ya está registrado.");
        }
    }

    // Este se mantiene igual (es lógica de autenticación pura)
    public User validateCredentials(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas."));
      
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Credenciales inválidas.");
        }
        return user;
    }   
}
