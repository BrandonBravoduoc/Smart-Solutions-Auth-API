package com.smart_solutions_auth.api.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart_solutions_auth.api.dto.user.UserContactDTO;
import com.smart_solutions_auth.api.dto.user.UserDTO;
import com.smart_solutions_auth.api.service.UserContactService;
import com.smart_solutions_auth.api.service.UserService;
import com.smart_solutions_auth.api.util.Validations;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final UserContactService userContactService;
    private final Validations validations;

    @PostMapping("/logout") 
    public ResponseEntity<?> logout(HttpServletResponse response) {
        userService.logout(response); 
        return ResponseEntity.ok().body("{\"message\": \"Sesión cerrada exitosamente\"}");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO.Response> getMyProfile() {
        Long userId = validations.getCurrentUserId();
        UserDTO.Response profile = userService.profile(userId);
        return ResponseEntity.ok(profile);
    }
    
    @PatchMapping("/profile/update")
    public ResponseEntity<UserContactDTO.Response> updateContact(
            @RequestBody UserContactDTO.UpdateRequest requestDto
    ) {
        UserContactDTO.Response updatedContact = userContactService.updateUserContact(requestDto);
        
        return ResponseEntity.ok(updatedContact);
    }

    @PatchMapping("/email")
        public ResponseEntity<UserDTO.UpdateEmailResponse> updateEmail(
                @Valid @RequestBody UserDTO.UpdateEmailRequest request,
                HttpServletResponse response) {
            
            Long userId = validations.getCurrentUserId();

            return ResponseEntity.ok(userService.updateEmail(userId, request, response));
    }

    @PutMapping("/deactivate")
    public ResponseEntity<String> deactivateAccount(
            @Valid @RequestBody UserDTO.DeactivateRequest request,
            HttpServletResponse response) {
            
        Long userId = validations.getCurrentUserId();
        
        userService.desactivateAccount(userId, request.password(), response);
        
        return ResponseEntity.ok("Cuenta desactivada exitosamente.");
    }

}
