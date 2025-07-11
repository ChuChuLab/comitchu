package com.commi.chu.domain.user.controller;

import com.commi.chu.domain.user.service.UserService;
import com.commi.chu.global.common.response.CommonResponse;
import com.commi.chu.global.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<CommonResponse<String>> test(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        // 1. userPrincipal 객체에서 사용자 ID를 꺼냅니다.
        Integer userId = userPrincipal.getId();

        // 2. 받아온 ID를 사용해서 응답 메시지를 만듭니다.
        String response = "인증 성공! 당신은 " + userId + "번 유저입니다. 고-수";

        return CommonResponse.ok(response);
    }

}
