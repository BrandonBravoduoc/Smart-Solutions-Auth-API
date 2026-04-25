package com.smart_solutions_auth.API.dto.user;

public class UserContactDTO {
    
    public record UpdateRequest(
        String name,
        String lastName,
        String phone
    ){}

    public record Response(
        String email,
        String name,
        String lastName,
        String phone
    ){}

}
