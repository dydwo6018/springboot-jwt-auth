package com.springbootjwtauth.controller;

import com.springbootjwtauth.dto.request.LoginRequestDto;
import com.springbootjwtauth.dto.request.SignupRequestDto;
import com.springbootjwtauth.dto.response.TokenResponseDto;
import com.springbootjwtauth.dto.response.UserResponseDto;
import com.springbootjwtauth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody @Valid SignupRequestDto request) {
        UserResponseDto response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        TokenResponseDto responseDto = authService.login(request);
        return ResponseEntity.ok(responseDto);
    }

    // 관리자 권한 부여
    @PatchMapping("/admin/users/{userId}/roles")
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 호출 가능
    public ResponseEntity<UserResponseDto> grantAdminRole(@PathVariable UUID userId) {
        UserResponseDto response = authService.grantAdminRole(userId);
        return ResponseEntity.ok(response);
    }

}
