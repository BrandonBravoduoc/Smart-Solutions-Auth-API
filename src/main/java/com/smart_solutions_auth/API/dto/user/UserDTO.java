package com.smart_solutions_auth.API.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDTO {

    public record RegisterRequest(
        @NotBlank(message = "El email es obligatorio.")
        @Email(message = "Formato de correo inválido.")
        String email,

        @NotBlank(message = "La contraseña es obligatoria.")
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", 
                 message = "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número")
        String password,

        @NotBlank(message = "Debe confirmar la contraseña.")
        String confirmPassword,

        @NotBlank(message = "El nombre es obligatorio.")
        String name,

        @NotBlank(message = "El apellido es obligatorio.")
        String lastName,

        @NotBlank(message = "El teléfono es obligatorio.")
        @Size(min = 9, max = 9, message = "El teléfono debe tener 9 dígitos.")
        String phone

    ){}

    public record Response(
        String email,
        String name,
        String lastName,
        String phone
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

}
