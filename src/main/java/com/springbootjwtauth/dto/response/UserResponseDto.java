package com.springbootjwtauth.dto.response;

import com.springbootjwtauth.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class UserResponseDto {

    private String username;
    private String nickname;
    private Set<Role> roles;
}
