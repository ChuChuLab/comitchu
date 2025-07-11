package com.commi.chu.global.security.auth;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class UserPrincipal implements OAuth2User {

    private final Integer id; // 우리 시스템의 User ID
    private final String githubUsername;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Map<String, Object> attributes;

    @Builder
    public UserPrincipal(Integer id, String githubUsername, Map<String, Object> attributes, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.githubUsername = githubUsername;
        // attributes가 null이면 빈 맵으로 초기화
        this.attributes = attributes != null ? attributes : Collections.emptyMap();

        // authorities가 외부에서 주입되면 그걸 사용하고, 아니면 기본값("ROLE_USER")을 사용
        if (authorities != null) {
            this.authorities = authorities;
        } else {
            this.authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getName() {
        // 이 메서드는 사용자의 고유 식별자를 반환해야 합니다. 여기서는 GitHub 닉네임을 사용합니다.
        return githubUsername;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}