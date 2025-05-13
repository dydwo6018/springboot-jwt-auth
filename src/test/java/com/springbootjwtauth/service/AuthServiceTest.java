package com.springbootjwtauth.service;

import com.springbootjwtauth.dto.request.LoginRequestDto;
import com.springbootjwtauth.dto.request.SignupRequestDto;
import com.springbootjwtauth.dto.response.TokenResponseDto;
import com.springbootjwtauth.dto.response.UserResponseDto;
import com.springbootjwtauth.model.Role;
import com.springbootjwtauth.model.User;
import com.springbootjwtauth.store.UserStore;
import com.springbootjwtauth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthServiceTest {


    @Autowired
    private AuthService authService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserStore userStore;

    @BeforeEach
    void setUp() {
        // 테스트 간 데이터 독립 보장
        userStore.getAll().clear();
    }

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

    @Test
    @DisplayName("로그인 성공 테스트")
    void login_success() {
        // given
        String username = "loginuser";
        String rawPassword = "1234";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .password(encodedPassword)
                .nickname("Tester2")
                .roles(Set.of(Role.USER))
                .build();

        userStore.save(user);

        LoginRequestDto request = new LoginRequestDto(username, rawPassword);

        // when
        TokenResponseDto response = authService.login(request);

        // then
        assertNotNull(response);
        assertNotNull(response.getToken());
        System.out.println("AccessToken = " + response.getToken());
    }

}

