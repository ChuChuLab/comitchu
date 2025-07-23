package com.commi.chu.domain.user.controller;

import com.commi.chu.domain.user.dto.response.TestResponse;
import com.commi.chu.domain.user.service.UserService;
import com.commi.chu.global.common.response.CommonResponse;
import com.commi.chu.global.security.auth.UserPrincipal;
import com.commi.chu.global.util.AuthenticationUtil;
import com.commi.chu.global.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationUtil authenticationUtil;

    /**
     * test용 api 입니다
     *
     * @return 고-수
     */
    @GetMapping("/test")
    public ResponseEntity<CommonResponse<TestResponse>> test (
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Integer userId =  authenticationUtil.getCurrentUserId(userPrincipal);

        return CommonResponse.ok(userService.getGoSuTest(userId));
    }

    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<Void>> logout() {
        ResponseCookie deletedAuthCookie = CookieUtil.deleteAuthCookie();

        return CommonResponse.okWithCookie(deletedAuthCookie);
    }

}
