package com.commi.chu.global.common.constants;

import com.commi.chu.global.config.DomainConfig;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityConstants {

    @Getter
    private static String domain;
    @Getter
    private static Boolean isLocal = false;

    private final DomainConfig domainConfig;

    @PostConstruct
    public void init() {
        domain = domainConfig.getDomain();
        isLocal = domainConfig.getIsLocal();
    }

    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 24L * 60 * 60; // 24시간
    public static final long REFRESH_TOKEN_VALIDITY_SECONDS = 14L * 24 * 60 * 60; // 14일

    // 쿠키 이름 상수
    public static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    public static final String ROLE_NAME = "typ";
    public static final String ROLE_PREFIX = "ROLE_";
}