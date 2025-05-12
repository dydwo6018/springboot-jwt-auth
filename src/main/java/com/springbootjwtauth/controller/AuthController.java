package com.springbootjwtauth.controller;

import com.springbootjwtauth.dto.request.LoginRequestDto;
import com.springbootjwtauth.dto.request.SignupRequestDto;
import com.springbootjwtauth.dto.response.TokenResponseDto;
import com.springbootjwtauth.dto.response.UserResponseDto;
import com.springbootjwtauth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
