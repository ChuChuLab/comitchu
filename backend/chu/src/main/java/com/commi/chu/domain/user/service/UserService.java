package com.commi.chu.domain.user.service;

import com.commi.chu.domain.user.dto.LoginResult;
import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.enums.Provider;
import com.commi.chu.global.error.code.ErrorCode;
import com.commi.chu.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    @Transactional
    public LoginResult handleOAuthLogin(Provider provider, String code) {
        // 1. 전략 가져오기
        OAuthStrategy strategy = oAuthStrategyMap.get(provider);
        if (strategy == null) {
            throw new CustomException(ErrorCode.INVALID_OAUTH_PROVIDER, "provider", provider.name());
        }

        // 2. 소셜 로그인으로 유저 정보 받아오기
        OAuthUserInfo userInfoFromOAuth = strategy.getUserInfo(code);

        // 3. 기존 회원인지 확인
        Optional<User> existingUser = userRepository.findByEmail(userInfoFromOAuth.getEmail());
        if (existingUser.isPresent() && existingUser.get().getUserType() == UserType.GENERAL) {
            throw new CustomException(ErrorCode.NOT_OAUTH_USER)
                    .addParameter("email", userInfoFromOAuth.getEmail());
        }

        // 4. 신규 회원이면 회원가입 진행
        User user = existingUser.orElseGet(() -> createOauthUser(userInfoFromOAuth, provider));
        String userId = user.getId().toString();

        String authToken = jwtTokenProvider.createAuthToken(userId);

        return LoginResult.of(authToken);
    }

}
