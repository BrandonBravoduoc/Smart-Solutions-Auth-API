package com.smart_solutions_auth.API.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.smart_solutions_auth.API.model.User;
import com.smart_solutions_auth.API.model.UserRole;
import com.smart_solutions_auth.API.repository.UserContactRepository;
import com.smart_solutions_auth.API.repository.UserRepository;
import com.smart_solutions_auth.API.repository.UserRoleRepository;

@Component
public class Validations {

    @Autowired 
    private UserRepository userRepository;

    @Autowired 
    private UserContactRepository userContactRepository;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRoleRepository userRoleRepository;

    public String emailValidate(String email) {
        if(userRepository.existsByEmail(email)){
            throw new RuntimeException("El correo electrónico ya está registrado.");
        }
        return email.trim().toLowerCase();
    }

    public void passwordValidate(String password, String confirmPassword) {
        if(!password.equals(confirmPassword)){
            throw new RuntimeException("Las contraseñas no coinciden.");
        }
    }

    public void contactValidate(String phoneNumber) {
        if(userContactRepository.existsByPhoneNumber(phoneNumber)){
            throw new RuntimeException("El número de teléfono ya está registrado.");
        }
    }

    public User validateCredentials(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Credenciales inválidas."));
      
        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Credenciales inválidas.");
        }
        return user;
    }   

    public void checkRole(String nameRole){
        if(userRoleRepository.findByNameRole(nameRole).isPresent()){
            throw new RuntimeException("El rol " + nameRole + " ya existe.");
        }
    }

    public UserRole roleVerification(String nameRole){
        return userRoleRepository.findByNameRole(nameRole)
            .orElseThrow(() -> new RuntimeException("El rol " + nameRole + " no está registrado en el sistema"));
    }

    public Long getCurrentUserId() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (details == null) {
            throw new RuntimeException("Sesión no válida");
        }
        return (Long) details;
    }

}
