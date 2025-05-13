package com.springbootjwtauth.service;

import com.springbootjwtauth.dto.request.LoginRequestDto;
import com.springbootjwtauth.dto.request.SignupRequestDto;
import com.springbootjwtauth.dto.response.TokenResponseDto;
import com.springbootjwtauth.dto.response.UserResponseDto;
import com.springbootjwtauth.exception.CustomException;
import com.springbootjwtauth.exception.ErrorCode;
import com.springbootjwtauth.model.Role;
import com.springbootjwtauth.model.User;
import com.springbootjwtauth.util.JwtUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Getter
@Service
@RequiredArgsConstructor
public class AuthService {

    // 암호화기 주입 (config에서 Bean 등록)
    private final BCryptPasswordEncoder passwordEncoder;
    // 메모리 저장소 (username → User)
    private final Map<String, User> userStore = new HashMap<>();

    private final JwtUtil jwtUtil;

    public UserResponseDto signup(SignupRequestDto requestDto) {

        // 1. 중복 체크
        if (userStore.containsKey(requestDto.getUsername())) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 2. 암호화된 비밀번호 생성
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // 3. User 객체 생성
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(requestDto.getUsername())
                .password(encodedPassword)
                .nickname(requestDto.getNickname())
                .roles(new HashSet<>(Set.of(Role.USER))) // 기본 USER 권한
                .build();

        // 4. 저장소에 저장
        userStore.put(user.getUsername(), user);

        // 5. 응답 객체 생성 및 반환
        return new UserResponseDto(
                user.getUsername(),
                user.getNickname(),
                user.getRoles());
    }


    public TokenResponseDto login(LoginRequestDto request) {

        // 1. 사용자가 입력한 username을 기반으로 저장소에서 사용자 조회하기
        User user = userStore.get(request.getUsername());

        // 2. 사용자가 존재하지 않거나 비밀번호가 틀리면 에러 발생 시키기
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        // 3. 로그인 성공한 경우 : AccessToken 생성
        String accessToken = jwtUtil.createAccessToken(user.getId());

        // 4. 응답 객체에 담아서 반환
        return new TokenResponseDto(accessToken);
    }
}
