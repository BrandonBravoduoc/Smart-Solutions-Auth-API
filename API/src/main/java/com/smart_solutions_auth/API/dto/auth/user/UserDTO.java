package com.smart_solutions_auth.API.dto.auth.user;

public class UserDTO {

    public record Response(
        Long id,
        String email
    ){}

    
}
