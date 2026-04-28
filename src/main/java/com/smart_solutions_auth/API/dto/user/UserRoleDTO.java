package com.smart_solutions_auth.API.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRoleDTO {
    
    public record CreateRoleRequest(
        @NotBlank(message = "El nombre del rol es obligatorio")
        @Size(min = 3, max = 20, message = "El nombre del rol debe tener entre 3 y 20 caracteres.")
        String nameRole
    ){}

    public record CreateRoleResponse(
        Long id,
        String nameRole,
        String message
    ){}
}
