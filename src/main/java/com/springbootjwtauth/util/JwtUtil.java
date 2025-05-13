package com.springbootjwtauth.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    // JWT를 만들 때 사용할 키
    private final Key key;

    // 토큰 유효 시간 (1시간 = 60분 = 60 * 60 * 1000ms)
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60L;

    // 생성자에서 yml에서 읽은 문자열 키를 암호화 키 객체로 바꿔줌
    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // JWT 토큰 생성 메서드
    public String createAccessToken(UUID userId) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setSubject(userId.toString()) // 토큰에 사용자 ID 넣기
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256) // 키로 서명
                .compact(); // 문자열로 압축
    }

    // JWT 토큰에서 사용자 ID 꺼내기
    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return UUID.fromString(claims.getSubject());
    }

    // JWT 토큰이 유효한지 확인하기
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
