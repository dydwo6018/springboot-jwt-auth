package com.springbootjwtauth.service;

import com.springbootjwtauth.dto.request.LoginRequestDto;
import com.springbootjwtauth.dto.request.SignupRequestDto;
import com.springbootjwtauth.dto.response.TokenResponseDto;
import com.springbootjwtauth.dto.response.UserResponseDto;
import com.springbootjwtauth.exception.CustomException;
import com.springbootjwtauth.exception.ErrorCode;
import com.springbootjwtauth.model.Role;
import com.springbootjwtauth.model.User;
import com.springbootjwtauth.store.UserStore;
import com.springbootjwtauth.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    @Autowired
    private JwtUtil jwtUtil;


    @Test
    @DisplayName("회원가입 성공 테스트")
    void 회원가입_성공() {
        // given
        Set<Role> roles = Set.of(Role.USER);
        SignupRequestDto request = new SignupRequestDto("testuser", "1234", "Tester", roles);

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

    @Test
    @DisplayName("회원가입 실패 - 이미 존재하는 사용자명")
    void 회원가입_실패_이미존재하는_유저명() {
        // given
        Set<Role> roles = Set.of(Role.USER);
        String username = "duplicateUser";
        SignupRequestDto request1 = new SignupRequestDto(username, "password", "닉네임1", roles);
        SignupRequestDto request2 = new SignupRequestDto(username, "password", "닉네임2", roles);

        // when
        authService.signup(request1);

        // then
        CustomException exception = assertThrows(CustomException.class, () -> authService.signup(request2));
        assertEquals(ErrorCode.USER_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void 로그인_실패_존재하지않는_유저() {
        // given
        LoginRequestDto request = new LoginRequestDto("notexist", "password");

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> authService.login(request));
        assertEquals(ErrorCode.INVALID_CREDENTIALS, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    void 로그인_실패_비밀번호불일치() {
        // given
        Set<Role> roles = Set.of(Role.USER);
        SignupRequestDto signupRequest = new SignupRequestDto("mismatchUser", "correctPassword", "닉네임", roles);
        authService.signup(signupRequest); // 회원가입 먼저

        LoginRequestDto loginRequest = new LoginRequestDto("mismatchUser", "wrongPassword");

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> authService.login(loginRequest));
        assertEquals(ErrorCode.INVALID_CREDENTIALS, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 성공 시 발급된 토큰이 유효한지 확인")
    void 로그인_성공_토큰_유효성_확인() {
        // given
        Set<Role> roles = Set.of(Role.USER);
        String username = "validUser";
        String password = "validPass";
        String nickname = "ValidNick";

        authService.signup(new SignupRequestDto(username, password, nickname, roles));
        LoginRequestDto request = new LoginRequestDto(username, password);

        // when
        TokenResponseDto tokenResponse = authService.login(request);

        // then
        assertNotNull(tokenResponse.getToken());
        assertDoesNotThrow(() -> jwtUtil.validateToken(tokenResponse.getToken()));
    }

    @Test
    @DisplayName("ADMIN 권한 부여 성공")
    void grant_admin_role_success() {
        // given
        Set<Role> roles = Set.of(Role.USER);
        SignupRequestDto signupRequest = new SignupRequestDto("adminuser", "1234", "관리자",roles);
        authService.signup(signupRequest);

        // when
        UserResponseDto updated = authService.grantAdminRole("adminuser");

        // then
        assertTrue(updated.getRoles().contains(Role.ADMIN));
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게 관리자 권한 부여 시 예외 발생")
    void grant_admin_role_user_not_found() {
        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.grantAdminRole("nouser");
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

}

