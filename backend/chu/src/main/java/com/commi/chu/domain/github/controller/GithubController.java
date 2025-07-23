package com.commi.chu.domain.github.controller;

import com.commi.chu.domain.github.dto.language.LanguageStatListResponse;
import com.commi.chu.domain.github.dto.language.LanguageStatsDto;
import com.commi.chu.domain.github.service.GithubLanguageService;
import com.commi.chu.global.common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GithubController {

    private final GithubLanguageService langService;

    /***
     * 사용 언어 비율이 제대로 나오는지 테스트용
     * @param username  GitHub 사용자 로그인 ID
     * @return Top10 언어,바이트수,비율 정보를 보여줌
     */
    @GetMapping("/{username}/languages")
    public ResponseEntity<CommonResponse<LanguageStatListResponse>> getLangs(@PathVariable String username) {
        LanguageStatListResponse result = langService.fetchLanguagePercentages(username);

        return CommonResponse.ok(result);
    }
}
