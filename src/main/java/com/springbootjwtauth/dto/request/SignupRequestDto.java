package com.springbootjwtauth.dto.request;

import lombok.Getter;

@Getter
public class SignupRequestDto {

    private String username;

    private String password;

    private String nickname;
}
