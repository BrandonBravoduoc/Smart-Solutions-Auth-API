package com.smart_solutions_auth.API.service;

import com.smart_solutions_auth.API.repository.UserContactRepository;
import com.smart_solutions_auth.API.repository.UserRepository;
import com.smart_solutions_auth.API.service.jwt.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smart_solutions_auth.API.dto.user.UserDTO;
import com.smart_solutions_auth.API.model.User;
import com.smart_solutions_auth.API.model.UserContact;
import com.smart_solutions_auth.API.model.UserRole;
import com.smart_solutions_auth.API.util.Validations;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {
 
    @Autowired
    private UserContactRepository userContactRepository;

    @Autowired
    private  UserRepository userRepository;
    
    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private Validations validations;

    @Autowired
    private JwtService jwtService;

    public UserDTO.Response userRegister(UserDTO.RegisterRequest dto, HttpServletResponse response){
        
        String cleanEmail = validations.emailValidate(dto.email());

        validations.passwordValidate(dto.password(), dto.confirmPassword());
        validations.contactValidate(dto.phone());
        
        UserRole role =  validations.roleVerification("CLIENTE");

        User user = new User();
        user.setEmail(cleanEmail);
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setUserRole(role);
        user.setAsset(true);

        User userSaved = userRepository.save(user);

        UserContact userContact = new UserContact();
        userContact.setName(dto.name());
        userContact.setLastName(dto.lastName());
        userContact.setPhoneNumber(dto.phone());
        userContact.setUser(userSaved);

        userContactRepository.save(userContact);

        String accessToken = jwtService.generateToken(userSaved);
        String refreshToken = jwtService.generateRefreshToken(userSaved);

        ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
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

        
        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, refreshCookie.toString());


        return new UserDTO.Response(
            user.getEmail(),
            userContact.getName(),
            userContact.getLastName(),
            userContact.getPhoneNumber()
        );
    }

    public UserDTO.ChangePasswordResponse changePassword(UserDTO.ChangePasswordRequest dto) {
        Long userId = validations.getCurrentUserId();
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        
        validations.passwordValidate(dto.newPassword(), dto.confirmNewPassword());

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña actual incorrecta.");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);

        return new UserDTO.ChangePasswordResponse("Contraseña actualizada exitosamente.");
    }

    public UserDTO.UpdateEmailResponse updateEmail(UserDTO.UpdateEmailRequest dto, HttpServletResponse response) {
        Long userId = validations.getCurrentUserId();
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta.");
        }

        String newEmail = validations.emailValidate(dto.newEmail());
        if (!dto.newEmail().equals(dto.confirmNewEmail())) {
            throw new RuntimeException("Los correos electrónicos no coinciden.");
        }

        user.setEmail(newEmail);
        userRepository.save(user);

        String newToken = jwtService.generateToken(user);
        ResponseCookie cookie = ResponseCookie.from("accessToken", newToken)
                .httpOnly(true)
                .secure(false) 
                .path("/")
                .maxAge(3600)
                .sameSite("Strict")
                .build();
        
        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString());

        return new UserDTO.UpdateEmailResponse(user.getEmail(), "Correo actualizado exitosamente.");
    }

    public void logout(HttpServletResponse response) {
        ResponseCookie accessCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/api/v1/auth/refresh")  
                .maxAge(0)
                .build();

        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, accessCookie.toString());
        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, refreshCookie.toString());
    }

    public boolean desactivateAccount(String password, HttpServletResponse response) {
        Long userId = validations.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Contraseña incorrecta.");
        }

        user.setAsset(false);
        userRepository.save(user);

        ResponseCookie deleteCookie = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(false) 
                .path("/")
                .maxAge(0) 
                .sameSite("Strict") 
                .build();
        
        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, deleteCookie.toString());
        
        return true;
    }

    public UserDTO.Response profile(){

        Long userId = validations.getCurrentUserId();

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        UserContact userContact = userContactRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Información de contacto no encontrada."));

        return new UserDTO.Response(
            user.getEmail(),
            userContact.getName(),
            userContact.getLastName(),
            userContact.getPhoneNumber()
        );
    }

    public UserDTO.Response updateByEmail(String emailToFind, UserDTO.UpdateUserByAdmin dto) {
            User user = userRepository.findByEmail(emailToFind)
                    .orElseThrow(() -> new RuntimeException("No se encontró el usuario con email: " + emailToFind));

            return processUpdate(user, dto);
        }

    public UserDTO.Response updateByPhone(String phoneToFind, UserDTO.UpdateUserByAdmin dto) {
            UserContact contact = userContactRepository.findByPhoneNumber(phoneToFind)
                    .orElseThrow(() -> new RuntimeException("No se encontró el usuario con teléfono: " + phoneToFind));
            
            User user = contact.getUser();

            return processUpdate(user, dto);
    }

    private UserDTO.Response processUpdate(User user, UserDTO.UpdateUserByAdmin dto) {
        UserContact contact = userContactRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Información de contacto no encontrada."));

        if (dto.email() != null && !dto.email().isBlank()) {
            user.setEmail(dto.email());
        }

        if (dto.password() != null && !dto.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.password()));
        }

        if (dto.name() != null && !dto.name().isBlank()) contact.setName(dto.name());
        if (dto.lastName() != null && !dto.lastName().isBlank()) contact.setLastName(dto.lastName());

        if (dto.phone() != null && !dto.phone().isBlank() && !dto.phone().equals(contact.getPhoneNumber())) {
            validations.contactValidate(dto.phone());
            contact.setPhoneNumber(dto.phone());
        }

        userRepository.save(user);
        userContactRepository.save(contact);

        return new UserDTO.Response(
                user.getEmail(),
                contact.getName(),
                contact.getLastName(),
                contact.getPhoneNumber()
        );
    }

    public UserDTO.Response getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));

        UserContact contact = userContactRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Detalles de contacto no encontrados para este usuario."));

        return new UserDTO.Response(
                user.getEmail(),
                contact.getName(),
                contact.getLastName(),
                contact.getPhoneNumber()
        );
    }

    public UserDTO.Response getUserByPhone(String phoneNumber) {
        UserContact contact = userContactRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("No existe un usuario con el teléfono: " + phoneNumber));

        User user = contact.getUser(); 

        return new UserDTO.Response(
                user.getEmail(),
                contact.getName(),
                contact.getLastName(),
                contact.getPhoneNumber()
        );
    }

}
