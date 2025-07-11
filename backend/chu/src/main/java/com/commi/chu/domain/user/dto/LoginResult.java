package com.commi.chu.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResult {
    private String authToken;

    public static LoginResult of(String authToken) {
        return LoginResult.builder()
                .authToken(authToken)
                .build();
    }
}