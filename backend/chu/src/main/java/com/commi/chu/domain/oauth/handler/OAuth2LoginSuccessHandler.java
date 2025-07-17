package com.commi.chu.domain.oauth.handler;

import com.commi.chu.global.security.auth.UserPrincipal;
import com.commi.chu.global.security.jwt.JwtTokenProvider;
import com.commi.chu.global.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 1. CustomOAuth2UserService에서 반환한 UserPrincipal 정보를 가져옵니다.
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Integer userId = userPrincipal.getId();

        // 2. 우리 서비스의 AuthToken을 생성합니다.
        String authToken = jwtTokenProvider.createAuthToken(userId.toString());

        // 3. 토큰을 쿠키에 담아 프론트엔드로 리디렉션 시킵니다.
        ResponseCookie authCookie = CookieUtil.makeAuthCookie(authToken);
        response.addHeader(HttpHeaders.SET_COOKIE, authCookie.toString());
        response.sendRedirect("http://localhost:5173");
    }
}