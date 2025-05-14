package com.springbootjwtauth.dto.request;

import com.springbootjwtauth.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class SignupRequestDto {

    private String username;

    private String password;

    private String nickname;

    private Set<Role> roles;
}
