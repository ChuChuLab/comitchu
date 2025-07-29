package com.commi.chu.domain.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GithubUserInfo {

    // GitHub API의 응답 필드 이름(snake_case)과 매핑
    @JsonProperty("login")
    private String githubUsername;

    private String email;

    @JsonProperty("avatar_url")
    private String avatarUrl;
}