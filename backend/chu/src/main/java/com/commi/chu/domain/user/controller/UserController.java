package com.commi.chu.domain.user.controller;

import com.commi.chu.domain.user.dto.LoginResult;
import com.commi.chu.domain.user.dto.response.LoginResponse;
import com.commi.chu.domain.user.enums.Provider;
import com.commi.chu.domain.user.service.UserService;
import com.commi.chu.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<CommonResponse<String>> test(
            @RequestParam(required = false) Integer age
    ){
        String response = "고-수";

        return CommonResponse.ok(response);
    }

    @GetMapping("/login/{provider}")
    public ResponseEntity<CommonResponse<LoginResponse>> socialLogin(
            @PathVariable String provider,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error
    ) {
        if (error != null) {
            return CommonResponse.redirect("");
        }

        Provider socialProvider = Provider.valueOf(provider.toUpperCase());
        LoginResult loginResult = userService.handleOAuthLogin(socialProvider, code);

        ResponseCookie accessTokenCookie = CookieUtil.makeAccessTokenCookie(loginResult.getAccessToken());
        ResponseCookie refreshTokenCookie = CookieUtil.makeRefreshTokenCookie(loginResult.getRefreshToken());

        return CommonResponse.redirectWithCookie("http://localhost:5173", accessTokenCookie, refreshTokenCookie);
    }

}
