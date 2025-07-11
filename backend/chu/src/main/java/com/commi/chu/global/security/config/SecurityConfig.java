package com.commi.chu.global.security.config;

import com.commi.chu.domain.oauth.handler.OAuth2LoginSuccessHandler;
import com.commi.chu.domain.user.service.CustomOAuth2UserService;
import com.commi.chu.global.security.DevAuthenticationFilter;
import com.commi.chu.global.security.jwt.JwtAuthenticationFilter;
import com.commi.chu.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Value("${app.domain.cors-origins}")
    private List<String> allowedOrigins;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired(required = false)
    private DevAuthenticationFilter devAuthenticationFilter;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CORS 설정을 직접 여기서 정의합니다.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 2. CSRF 보호 기능을 비활성화합니다.
                .csrf(AbstractHttpConfigurer::disable)
                // 3. 지금은 세션 관리 정책을 설정하지 않아도 괜찮습니다. (주석 처리 또는 삭제)
                // .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. 모든 요청을 허용하도록 변경합니다.
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/api/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        // 1. 로그인 성공 후 사용자 정보를 처리할 서비스 지정
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        // 2. 로그인이 최종 성공했을 때 처리할 핸들러 지정
                        .successHandler(oAuth2LoginSuccessHandler)
                );

        // 개발 환경 필터는 조건부로 추가
//        if (isDevOrLocalProfile() && devAuthenticationFilter != null) {
        log.info("개발 환경 인증 필터를 추가합니다.");
        http.addFilterBefore(devAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        }
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 프론트엔드 개발 서버 주소(React)를 허용합니다.
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더를 허용합니다.
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 이 설정을 적용합니다.
        return source;
    }
}