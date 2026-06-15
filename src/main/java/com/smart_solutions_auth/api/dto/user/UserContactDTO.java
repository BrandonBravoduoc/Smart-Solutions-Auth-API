package com.smart_solutions_auth.api.dto.user;

import jakarta.validation.constraints.Pattern;

public class UserContactDTO {
    
    public record UpdateRequest(
        @Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s]{2,50}$", message = "El nombre solo puede contener letras y espacios, sin números, y debe tener entre 2 y 50 caracteres.")
        String name,
        @Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑ\\s]{2,50}$", message = "El apellido solo puede contener letras y espacios, sin números, y debe tener entre 2 y 50 caracteres.")
        String lastName,
        @Pattern(regexp = "^\\d{9}$", message = "El teléfono debe contener exactamente 9 dígitos sin espacios ni caracteres especiales.")
        String phone
    ){}

    public record Response(
        String email,
        String name,
        String lastName,
        String phone
    ){}

}
