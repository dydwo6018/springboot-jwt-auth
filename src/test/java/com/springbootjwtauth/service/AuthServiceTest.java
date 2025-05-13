package com.springbootjwtauth.service;

import com.springbootjwtauth.dto.request.SignupRequestDto;
import com.springbootjwtauth.dto.response.UserResponseDto;
import com.springbootjwtauth.model.Role;
import com.springbootjwtauth.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AuthServiceTest {


    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void 회원가입_성공() {
        // given
        SignupRequestDto request = new SignupRequestDto("testuser", "1234", "Tester");

        // when
        UserResponseDto response = authService.signup(request);

        // then
        assertEquals("testuser", response.getUsername());
        assertEquals("Tester", response.getNickname());
        assertTrue(response.getRoles().contains(Role.USER));
    }
}

