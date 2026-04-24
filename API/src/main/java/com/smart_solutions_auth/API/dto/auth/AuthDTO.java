package com.smart_solutions_auth.API.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDTO {
    

    public record LoginRequest(
        @NotBlank(message = "El correo es obligatorio.")
        @Email(message = "Debe ingresar un formato de correo válido.")
        String email,
        
        @NotBlank(message = "La contraseña es obligatoria.")
        String password
    ){}

    public record Response(
        String email,
        String role
    ){}
}
