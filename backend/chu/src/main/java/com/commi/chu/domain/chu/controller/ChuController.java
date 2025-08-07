package com.commi.chu.domain.chu.controller;

import java.util.List;

import com.commi.chu.domain.chu.dto.BackgroundRequestDto;
import com.commi.chu.domain.chu.dto.ChuSkinListResponseDto;
import com.commi.chu.domain.chu.dto.MainChuResponseDto;
import com.commi.chu.domain.chu.dto.UpdateBackgroundResponseDto;
import com.commi.chu.domain.chu.dto.UpdateMainChuResponseDto;
import com.commi.chu.domain.chu.scheduler.LanguageUnlockScheduler;
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
    private final LanguageUnlockScheduler languageUnlockScheduler;
    private final AuthenticationUtil authenticationUtil;

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

    /***
     * 사용자가 가진 chu 스킨 목록을 조회하는 메서드
     *
     * @param userPrincipal 요청한 사용자 정보
     * @return 사용자가 가진 chu 스킨 목록을 가진 ResponseEntity
     */
    @GetMapping("/list")
    public ResponseEntity<CommonResponse<List<ChuSkinListResponseDto>>> getChuSkinList(
        @AuthenticationPrincipal UserPrincipal userPrincipal
    ){
        Integer userId = authenticationUtil.getCurrentUserId(userPrincipal);

        return CommonResponse.ok(chuService.getUserChuSkins(userId));
    }

    /***
     * 대표 chu의 언어를 바꾸는 메서드
     *
     * @param userPrincipal 요청한 사용자 정보
     * @param langId 대표 언어로 지정할 langId
     * @return 대표 언어 변경 완료 메시지
     */
    @PatchMapping("/main/{langId}")
    public ResponseEntity<CommonResponse<UpdateMainChuResponseDto>> updateMainChu(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PathVariable Integer langId

    ){
        Integer userId = authenticationUtil.getCurrentUserId(userPrincipal);

        return CommonResponse.ok(chuService.updateMainChu(userId,langId));
    }

    /***
     * 해금 언어 스케줄러를 api 호출로 실행시키기 위한 메서드
     *
     * @return 언어 해금
     */
    @PatchMapping("/admin/unlock")
    public ResponseEntity<?> unlockSkin(){

        languageUnlockScheduler.dailyUnlockScheduler();

        return CommonResponse.ok("언어 해금 완료");
    }

    /***
     * 배경화면을 변경하는 메서드
     *
     * @param userPrincipal 요청한 사용자 정보
     * @param backgroundRequestDto 변경할 배경화면 이름
     * @return 배경화면 변경 성공,실패 응답
     */
    @PatchMapping("/background")
    public ResponseEntity<CommonResponse<UpdateBackgroundResponseDto>> updateBackground(
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @RequestBody BackgroundRequestDto backgroundRequestDto
    ){

        Integer userId = authenticationUtil.getCurrentUserId(userPrincipal);

        return CommonResponse.ok(chuService.updateBackgroundImage(userId,backgroundRequestDto));
    }
}
