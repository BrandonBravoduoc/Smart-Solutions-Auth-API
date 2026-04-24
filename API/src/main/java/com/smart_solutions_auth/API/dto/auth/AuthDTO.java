package com.smart_solutions_auth.API.dto.auth;


public class AuthDTO {
    

    public record LoginRequest(
        String email,
        String password
    ){}

    public record Response(
        String token,
        UserDTO.Response user
    ){}
}
