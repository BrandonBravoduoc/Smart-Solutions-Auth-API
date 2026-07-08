package com.smart_solutions_auth.api.dto.user;

import jakarta.validation.constraints.Pattern;

public class UserContactDTO {
    
    public record UpdateRequest(
        @Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑüÜ\\s]{3,50}$", message = "El nombre solo puede contener letras y espacios, sin números, y debe tener entre 3 y 50 caracteres.")
        String name,
        @Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑüÜ\\s]{3,50}$", message = "El apellido solo puede contener letras y espacios, sin números, y debe tener entre 3 y 50 caracteres.")
        String lastName,
        @Pattern(regexp = "^[92]\\d{8}$", message = "El teléfono debe tener 9 dígitos y comenzar con 9 o 2.")
        String phone
    ){}

    public record Response(
        String email,
        String name,
        String lastName,
        String phone
    ){}

}
