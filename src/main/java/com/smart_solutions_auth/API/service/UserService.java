package com.smart_solutions_auth.API.service;

import com.smart_solutions_auth.API.repository.UserContactRepository;
import com.smart_solutions_auth.API.repository.UserRepository;
import com.smart_solutions_auth.API.service.jwt.JwtService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smart_solutions_auth.API.dto.user.UserDTO;
import com.smart_solutions_auth.API.model.User;
import com.smart_solutions_auth.API.model.UserContact;
import com.smart_solutions_auth.API.model.UserRole;
import com.smart_solutions_auth.API.util.Validations;

import jakarta.servlet.http.Cookie;
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

    public UserDTO.Response userRegister(UserDTO.RegisterRequest dto){
        
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

        return new UserDTO.Response(
            user.getEmail(),
            userContact.getName(),
            userContact.getLastName(),
            userContact.getPhoneNumber()
        );
    }

    public UserDTO.ChangePasswordResponse changePassword(UserDTO.ChangePasswordRequest dto){
       
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
        
        validations.passwordValidate(dto.newPassword(), dto.confirmNewPassword());

        if(!passwordEncoder.matches(dto.currentPassword(), user.getPassword())){
            throw new RuntimeException("Contraseña actual incorrecta.");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);

        return new UserDTO.ChangePasswordResponse("Contraseña actualizada exitosamente.");
    }

    public UserDTO.UpdateEmailResponse updateEmail(UserDTO.UpdateEmailRequest dto, HttpServletResponse response){

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if(!passwordEncoder.matches(dto.password(), user.getPassword())){
            throw new RuntimeException("Contraseña incorrecta.");
        }

        String newEmail = validations.emailValidate(dto.newEmail());
        if(!dto.newEmail().equals(dto.confirmNewEmail())){
            throw new RuntimeException("Los correos electrónicos no coinciden.");
        }

        user.setEmail(newEmail);
        userRepository.save(user);

        String newToken = jwtService.generateToken(user);

        Cookie cookie = new Cookie("accessToken", newToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);

        return new UserDTO.UpdateEmailResponse(
            user.getEmail(),
            "Correo electrónico actualizado exitosamente."
        );

    }

    public void logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("accessToken", "")
            .httpOnly(true)
            .secure(false)
            .path("/")
            .maxAge(0)
            .sameSite("Strict")
            .build();

        response.addHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public boolean desactivateAccount(String password, HttpServletResponse response) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new RuntimeException("Contraseña incorrecta.");
        }

        user.setAsset(false);
        userRepository.save(user);

        Cookie cookie = new Cookie("accessToken","");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        return true;

    }
}
