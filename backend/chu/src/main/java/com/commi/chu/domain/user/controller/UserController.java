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
    public ResponseEntity<CommonResponse<String>> test() {

        String response = "인증 성공! 당신은 고-수";

        return CommonResponse.ok(response);
    }

}
