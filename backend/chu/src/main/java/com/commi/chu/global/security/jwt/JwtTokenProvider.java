package com.commi.chu.global.security.jwt;

import com.commi.chu.global.security.auth.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long authTokenValidityMilliseconds;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.auth-token-validity-seconds}") long authTokenValiditySeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.authTokenValidityMilliseconds = authTokenValiditySeconds * 1000;
    }

    // --- 변경: userType 파라미터 삭제 ---
    public String createAuthToken(String userId) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.authTokenValidityMilliseconds);

        return Jwts.builder()
                .subject(userId)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    // --- 변경: getAuthentication 메서드 간소화 ---
    public Authentication getAuthentication(String token) {
        String userId = getUserId(token);

        // UserPrincipal을 만들 때 가장 핵심적인 정보인 id만 사용합니다.
        UserPrincipal userPrincipal = UserPrincipal.builder()
                .id(Integer.valueOf(userId))
                .build();

        // 권한은 기본 'ROLE_USER' 하나만 부여합니다.
        return new UsernamePasswordAuthenticationToken(userPrincipal, "",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    /**
     * 토큰에서 사용자 ID를 추출합니다.
     */
    public String getUserId(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * 토큰의 유효성을 검증하고 결과를 TokenValidationResult 객체로 반환합니다.
     */
    public TokenValidationResult validateToken(String token) {
        try {
            extractAllClaims(token);
            // 예외가 발생하지 않으면 토큰은 유효함
            return new TokenValidationResult(true, null);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우
            log.warn("Expired JWT token: {}", e.getMessage());
            return new TokenValidationResult(false, TokenError.EXPIRED);
        } catch (Exception e) {
            // 그 외 모든 유효하지 않은 토큰의 경우
            log.warn("Invalid JWT token: {}", e.getMessage());
            return new TokenValidationResult(false, TokenError.INVALID);
        }
    }

    /**
     * 토큰에서 모든 정보를 담은 Claims를 추출합니다.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}