package com.springbootjwtauth.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class User {
    private UUID id;
    private String username;
    private String password;
    private String nickname;
    private Set<Role> roles;
}
