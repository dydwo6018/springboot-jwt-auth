package com.springbootjwtauth.dto.response;

public class TokenResponseDto {
    private String token;

    public TokenResponseDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
