package com.commi.chu.domain.user.service;

import com.commi.chu.domain.chu.entity.Chu;
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
        log.info("CustomOAuth2UserService 시작!"); // 1. 시작 로그

        // 1. 기본 OAuth2User 객체를 받아옵니다.
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        log.info("GitHub 사용자 정보: {}", attributes); // 2. 받아온 정보 로그

        // 2. GitHub 사용자 정보를 추출합니다. (고유 ID, 닉네임, 이메일, 프로필사진)
        // githubId는 Long 타입일 수 있으므로, Number로 받고 longValue()로 변환합니다.
        Long githubId = ((Number) attributes.get("id")).longValue();
        String githubUsername = (String) attributes.get("login");
        String email = (String) attributes.get("email");
        String avatarUrl = (String) attributes.get("avatar_url");

        // 3. githubId를 기준으로 우리 DB에 사용자가 있는지 조회하거나, 없으면 새로 생성합니다.
        User user = userRepository.findByGithubId(githubId)
                .map(existingUser -> {
                    // 이미 가입한 회원이면, 정보 업데이트 (예: 프로필 사진 변경 등)
                    log.info("기존 회원을 찾았습니다: {}", existingUser.getGithubUsername()); // 3-1. 기존 회원 로그
                    existingUser.updateProfile(githubUsername, avatarUrl);
                    return existingUser;
                })
                .orElseGet(() -> {
                    log.info("신규 회원입니다. DB 저장을 시작합니다."); // 3-2. 신규 회원 로그
                    // 신규 회원이면...
                    // 3-1. User 엔티티를 생성합니다.
                    User newUser = User.builder()
                            .githubId(githubId)
                            .githubUsername(githubUsername)
                            .email(email)
                            .avatarUrl(avatarUrl)
                            .build();

                    userRepository.save(newUser);
                    log.info("User 저장 성공: {}", newUser.getGithubUsername()); // 4. User 저장 로그

                    // 3-2. 해당 유저의 기본 '츄'를 생성하고 저장합니다.
                    Chu newChu = Chu.builder()
                            .user(newUser)
                            .name(githubUsername + "의 츄")
                            .level(1)
                            .exp(0)
                            .type("NORMAL") // 기본 타입
                            .build();

                    chuRepository.save(newChu);
                    log.info("Chu 저장 성공: {}", newChu.getName()); // 5. Chu 저장 로그

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