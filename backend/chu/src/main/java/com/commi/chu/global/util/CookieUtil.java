package com.commi.chu.global.util;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public final class CookieUtil {

    // 클래스가 인스턴스화되는 것을 방지
    private CookieUtil() {
    }

    /**
     * 인증 토큰을 담은 쿠키를 생성합니다.
     * @param authToken 우리 서비스의 인증 토큰
     * @return ResponseCookie 객체
     */
    public static ResponseCookie makeAuthCookie(String authToken) {
        return ResponseCookie.from("auth_token", authToken)
                .httpOnly(true) // JavaScript에서 접근 불가
                .secure(true)   // HTTPS 통신에서만 전송
                .path("/")      // 모든 경로에서 사용
                .maxAge(Duration.ofDays(7)) // 쿠키 유효 기간 7일로 설정
                .sameSite("Strict") // CSRF 공격 방지
                .build();
    }

    /**
     * 인증 쿠키를 삭제하는 쿠키를 생성합니다.
     * @return 만료 시간이 0인 ResponseCookie 객체
     */
    public static ResponseCookie deleteAuthCookie() {
        return ResponseCookie.from("auth_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // 유효 시간을 0으로 설정하여 즉시 만료
                .sameSite("Strict")
                .build();
    }
}