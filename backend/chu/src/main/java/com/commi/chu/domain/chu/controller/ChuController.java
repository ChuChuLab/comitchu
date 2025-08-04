package com.commi.chu.domain.chu.controller;

import com.commi.chu.domain.chu.service.ChuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chu")
@RequiredArgsConstructor
public class ChuController {

    private final ChuService chuService;

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

}
