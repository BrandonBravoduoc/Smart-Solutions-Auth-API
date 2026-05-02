package com.smart_solutions_auth.API.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_solutions_auth.API.dto.user.UserContactDTO;
import com.smart_solutions_auth.API.dto.user.UserDTO;
import com.smart_solutions_auth.API.service.UserContactService;
import com.smart_solutions_auth.API.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final UserContactService userContactService;

    @PostMapping("/logout") 
    public ResponseEntity<?> logout(HttpServletResponse response) {
        userService.logout(response); 
        return ResponseEntity.ok().body("{\"message\": \"Sesión cerrada exitosamente\"}");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO.Response> getMyProfile() {
        UserDTO.Response profile = userService.profile();
                return ResponseEntity.ok(profile);
    }
    
    @PatchMapping("/profile/update")
    public ResponseEntity<UserContactDTO.Response> updateContact(
            @RequestBody UserContactDTO.UpdateRequest requestDto
    ) {
        UserContactDTO.Response updatedContact = userContactService.updateUserContact(requestDto);
        
        return ResponseEntity.ok(updatedContact);
    }

}
