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



}
