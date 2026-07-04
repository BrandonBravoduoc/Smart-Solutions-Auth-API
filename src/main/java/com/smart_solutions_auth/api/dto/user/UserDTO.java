package com.smart_solutions_auth.api.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDTO {

    public record RegisterRequest(
        @NotBlank(message = "El email es obligatorio.")
        @Size(max = 100, message = "El correo no puede superar los 100 caracteres.")
        @Email(message = "Formato de correo inválido.")
        String email,

        @NotBlank(message = "La contraseña es obligatoria.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
                 message = "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número")
        String password,

        @NotBlank(message = "Debe confirmar la contraseña.")
        String confirmPassword,

        @NotBlank(message = "El nombre es obligatorio.")
        @Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑüÜ\\s]{2,50}$", message = "El nombre solo puede contener letras y espacios, sin números, y debe tener entre 2 y 50 caracteres.")
        String name,

        @NotBlank(message = "El apellido es obligatorio.")
        @Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑüÜ\\s]{2,50}$", message = "El apellido solo puede contener letras y espacios, sin números, y debe tener entre 2 y 50 caracteres.")
        String lastName,

        @NotBlank(message = "El teléfono es obligatorio.")
        @Pattern(regexp = "^[92]\\d{8}$", message = "El teléfono debe tener 9 dígitos y comenzar con 9 o 2.")
        String phone,

        @NotNull(message ="Debe seleccionar una suscursal.")
        Long addressId


    ){}

    public record Response(
        Long id,
        String email,
        String name,
        String lastName,
        String phone,
        String sucursalName,
        String role
    ){}

    //-------------------------------------------------------------------------------------------------------

    public record ChangePasswordRequest(

        @NotBlank(message = "La contraseña actual es obligatoria.")
        String currentPassword,

        @NotBlank(message = "La nueva contraseña es obligatoria.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", 
                 message = "La nueva contraseña debe tener al menos 8 caracteres, una mayúscula y un número.")
        String newPassword,

        @NotBlank(message = "Debe confirmar la nueva contraseña.")
        String confirmNewPassword
    ){}

    public record ChangePasswordResponse(
        String email
    ){}


    //-------------------------------------------------------------------------------------------------------

    public record UpdateEmailRequest(
        @NotBlank(message = "El email es obligatorio.")
        @Size(max = 100, message = "El correo no puede superar los 100 caracteres.")
        @Email(message = "Formato de correo inválido.")
        String newEmail,

        @NotBlank(message = "Debe confirmar el nuevo email.")
        String confirmNewEmail,

        @NotBlank(message = "Debe ingresar su contraseña para confirmar el cambio.")
        String password 
    ){}

    public record UpdateEmailResponse(
        String email,
        String message
    ){}

    //-------------------------------------------------------------------------------------------------------

    public record UpdateUserByAdmin(
        String currentEmail,
        @Size(max = 100, message = "El correo no puede superar los 100 caracteres.")
        @Email(message = "Formato de correo inválido.")
        String email,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
                 message = "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número")
        String password,

        @Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑüÜ\\s]{2,50}$", message = "El nombre solo puede contener letras y espacios, sin números, y debe tener entre 2 y 50 caracteres.")
        String name,
        @Pattern(regexp = "^[a-zA-ZáéíóúñÁÉÍÓÚÑüÜ\\s]{2,50}$", message = "El apellido solo puede contener letras y espacios, sin números, y debe tener entre 2 y 50 caracteres.")
        String lastName,
        @Pattern(regexp = "^[92]\\d{8}$", message = "El teléfono debe tener 9 dígitos y comenzar con 9 o 2.")
        String phone,
        Long addressId
    ){}


    //-------------------------------------------------------------------------------------------------------
    
    public record DeactivateRequest(
        @NotBlank(message = "La contraseña es obligatoria para desactivar la cuenta.")
        String password
    ){}
}
