package com.commi.chu.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TestResponse {
    private final Integer userId;
    private final String comment;
}
