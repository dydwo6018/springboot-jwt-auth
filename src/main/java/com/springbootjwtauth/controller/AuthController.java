package com.springbootjwtauth.controller;

import com.springbootjwtauth.dto.request.LoginRequestDto;
import com.springbootjwtauth.dto.request.SignupRequestDto;
import com.springbootjwtauth.dto.response.TokenResponseDto;
import com.springbootjwtauth.dto.response.UserResponseDto;
import com.springbootjwtauth.exception.CustomException;
import com.springbootjwtauth.exception.ErrorCode;
import com.springbootjwtauth.model.User;
import com.springbootjwtauth.service.AuthService;
import com.springbootjwtauth.store.UserStore;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    private final AuthService authService;
    private final UserStore userStore;

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody @Valid SignupRequestDto request) {
        UserResponseDto response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        TokenResponseDto responseDto = authService.login(request);
        return ResponseEntity.ok(responseDto);
    }

    // 관리자 권한 부여 API
    @PatchMapping("/admin/users/{username}/roles")
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 호출 가능
    public ResponseEntity<UserResponseDto> grantAdminRole(@PathVariable String username) {
        UserResponseDto responseDto = authService.grantAdminRole(username);
        return ResponseEntity.ok(responseDto);
    }

    // 권한체크를 위한 API
    @GetMapping("/me")
    public UserResponseDto getMyInfo(@AuthenticationPrincipal String username) {
        User user = userStore.findByUsername(username);
        if (user == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        return new UserResponseDto(user.getUsername(), user.getNickname(), user.getRoles());
    }

}
