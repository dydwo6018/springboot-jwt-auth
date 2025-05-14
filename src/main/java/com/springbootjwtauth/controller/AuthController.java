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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 및 권한 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {

    private final AuthService authService;
    private final UserStore userStore;

    // 회원가입 API
    @Operation(summary = "회원가입", description = "회원가입 API 입니다.")
    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@RequestBody @Valid SignupRequestDto request) {
        UserResponseDto response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    // 로그인 API
    @Operation(summary = "로그인", description = "로그인 API 입니다.")
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto request) {
        TokenResponseDto responseDto = authService.login(request);
        return ResponseEntity.ok(responseDto);
    }

    // 관리자 권한 부여 API
    @Operation(
            summary = "관리자 권한 부여",
            description = "해당 유저에게 관리자 권한을 부여하는 API 입니다.(ADMIN 전용)",
            security = { @SecurityRequirement(name = "JWT Auth") }
    )
    @PatchMapping("/admin/users/{username}/roles")
    @PreAuthorize("hasRole('ADMIN')") // 관리자만 호출 가능
    public ResponseEntity<UserResponseDto> grantAdminRole(@PathVariable String username) {
        UserResponseDto responseDto = authService.grantAdminRole(username);
        return ResponseEntity.ok(responseDto);
    }

    // 권한체크를 위한 API
    @Operation(summary = "내 정보 조회",
            description = "로그인한 사용자의 정보를 조회하는 API 입니다.",
            security = { @SecurityRequirement(name = "JWT Auth") }
    )
    @GetMapping("/me")
    public UserResponseDto getMyInfo(@AuthenticationPrincipal String username) {
        User user = userStore.findByUsername(username);
        if (user == null) throw new CustomException(ErrorCode.USER_NOT_FOUND);
        return new UserResponseDto(user.getUsername(), user.getNickname(), user.getRoles());
    }

}
