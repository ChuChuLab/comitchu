package com.commi.chu.domain.chu.controller;

import com.commi.chu.domain.chu.dto.MainChuResponseDto;
import com.commi.chu.domain.chu.service.ChuService;
import com.commi.chu.global.common.response.CommonResponse;
import com.commi.chu.global.security.auth.UserPrincipal;
import com.commi.chu.global.util.AuthenticationUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chu")
@RequiredArgsConstructor
public class ChuController {

    private final ChuService chuService;
    private final AuthenticationUtil authenticationUtil;

    /**
     * 사용자 레벨이 표시된 커밋츄 뱃지 이미지를 반환합니다.
     * 모든 로직과 CustomException 처리는 ChuService와 GlobalExceptionHandler에 위임됩니다.
     *
     * @return PNG 이미지 바이트 배열을 포함하는 ResponseEntity
     */
    @GetMapping(value = "/test/{githubUsername}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<String> getCommitchuBadge( @PathVariable String githubUsername,
                                                     @RequestParam String background,
                                                     @RequestParam String character) {
        try {
            String svgString = chuService.generateTestCommitchuLevelBadge(githubUsername, background, character);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("image/svg+xml")); // HTTP 헤더도 SVG 타입으로 설정
            return new ResponseEntity<>(svgString, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 사용자의 커밋츄 뱃지 이미지를 반환합니다.
     *
     * @return PNG 이미지 바이트 배열을 포함하는 ResponseEntity
     */
    @GetMapping(value = "/{githubUsername}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<String> getChu(@PathVariable String githubUsername) {
        try {
            String svgString = chuService.generateCommitchuLevelBadge(githubUsername);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("image/svg+xml"));
            return new ResponseEntity<>(svgString, headers, HttpStatus.OK);
        } catch (Exception e) {
            throw e;
        }
    }

    /***
     * 사용자의 대표 chu 정보를 반환합니다.
     *
     * @param userPrincipal 요청한 사용자 정보
     * @return 사용자의 대표 chu 정보를 포함한 ResponseEntity
     */
    @GetMapping("/main")
    public ResponseEntity<CommonResponse<MainChuResponseDto>> getMainChu(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        Integer userId = authenticationUtil.getCurrentUserId(userPrincipal);

        return CommonResponse.ok(chuService.getMainChu(userId));
    }

}
