package com.commi.chu.domain.github.controller;

import com.commi.chu.domain.github.dto.language.LanguageStatListResponse;
import com.commi.chu.domain.github.scheduler.GithubStatScheduler;
import com.commi.chu.domain.github.service.GithubLanguageService;
import com.commi.chu.global.common.response.CommonResponse;
import com.commi.chu.global.security.auth.UserPrincipal;
import com.commi.chu.global.util.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GithubController {

    private final GithubLanguageService langService;
    private final GithubStatScheduler statScheduler;
    private final AuthenticationUtil authenticationUtil;

    /***
     * 유저의 사용언어 정보, 비율, 바이트 수 반환하는 api
     * @param userPrincipal 로그인한 유저 정보
     * @return 사용자의 사용 언어 종류, 바이트 수, 비율,
     */
    @GetMapping("/languages")
    public ResponseEntity<CommonResponse<LanguageStatListResponse>> getLangs(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Integer userId = authenticationUtil.getCurrentUserId(userPrincipal);
        LanguageStatListResponse result = langService.fetchLanguagePercentages(userId);

        return CommonResponse.ok(result);
    }


    @GetMapping("/stat")
    public ResponseEntity<CommonResponse<String>> schedularStat() {
        statScheduler.updateAllGithubStats();

        log.info("github Stat 스케줄러 API");

        return CommonResponse.ok("github stat 스케줄러 돌림");
    }
}
