package com.commi.chu.domain.user.service;

import com.commi.chu.domain.chu.entity.Chu;
import com.commi.chu.domain.chu.entity.ChuStatus;
import com.commi.chu.domain.chu.repository.ChuRepository;
import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;
import com.commi.chu.global.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final ChuRepository chuRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. 기본 OAuth2User 객체를 받아옵니다.
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 2. GitHub 사용자 정보를 추출합니다. (고유 ID, 닉네임, 프로필사진)
        // githubId는 Long 타입일 수 있으므로, Number로 받고 longValue()로 변환합니다.
        Long githubId = ((Number) attributes.get("id")).longValue();
        String githubUsername = (String) attributes.get("login");
        String avatarUrl = (String) attributes.get("avatar_url");

        // 3. githubId를 기준으로 우리 DB에 사용자가 있는지 조회하거나, 없으면 새로 생성합니다.
        User user = userRepository.findByGithubId(githubId)
                .map(existingUser -> {
                    // 이미 가입한 회원이면, 정보 업데이트 (예: 프로필 사진 변경 등)
                    existingUser.updateProfile(githubUsername, avatarUrl);
                    return existingUser;
                })
                .orElseGet(() -> {
                    // 신규 회원이면...
                    // 3-1. User 엔티티를 생성합니다.
                    User newUser = User.builder()
                            .githubId(githubId)
                            .githubUsername(githubUsername)
                            .avatarUrl(avatarUrl)
                            .build();

                    userRepository.save(newUser);

                    // 3-2. 해당 유저의 기본 '츄'를 생성하고 저장합니다.
                    Chu newChu = Chu.builder()
                            .user(newUser)
                            .name(githubUsername + "의 츄")
                            .level(1)
                            .exp(0)
                            .status(ChuStatus.NORMAL) //츄의 상태는 기본값 NORAML
                            .lang("comit") //첫번째 츄는 대표캐릭터
                            .background("flower")
                            .build();

                    chuRepository.save(newChu);

                    return newUser;
                });

        log.info("UserPrincipal 반환 직전"); // 6. 종료 로그
        // 4. Spring Security가 관리할 우리 시스템의 사용자 정보 객체(UserPrincipal)를 반환합니다.
        return UserPrincipal.builder()
                .id(user.getId()) // 우리 시스템의 User ID (PK)
                .githubUsername(user.getGithubUsername())
                .attributes(attributes) // GitHub 원본 속성 정보
                .build();
    }
}