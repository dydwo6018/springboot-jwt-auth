package com.springbootjwtauth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignupRequestDto {

    private String username;

    private String password;

    private String nickname;
}
