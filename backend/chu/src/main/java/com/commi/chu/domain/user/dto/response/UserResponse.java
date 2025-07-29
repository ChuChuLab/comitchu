package com.commi.chu.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private final String userName;
    private final String avatarUrl;
}
