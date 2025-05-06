package com.kerem.dto.request;

public record UserLoginRequestDTO(
        String email,
        String password
) {
}
