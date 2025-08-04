package com.commi.chu.domain.chu.service;

import com.commi.chu.domain.chu.entity.ChuStatus;
import com.commi.chu.global.exception.CustomException;
import com.commi.chu.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BadgeGeneratorService {

    private final ResourceLoader resourceLoader;

    private static final String BACKGROUND_IMAGE_BASE_PATH = "images/backgrounds/";
    private static final String CHARACTER_IMAGE_BASE_PATH = "images/chu/";

    // --- 애니메이션 관련 상수 ---
    private static final String X_ANIMATION_DURATION = "6s";
    private static final String ANIMATION_REPEAT_COUNT = "indefinite";
    private static final int CHARACTER_HORIZONTAL_MOVEMENT_PIXELS = 15;

    @Cacheable(value = "badgeSvg", key = "#githubUsername + '_' + #backgroundName + '_' + #lang")
    public String generateSvgBadge(String githubUsername, String backgroundName, String lang, String status) {

        String dir = "normal/";
        if(status.equals("HUNGRY"))     dir = "hungry/";
        else if(status.equals("HAPPY"))    dir = "happy/";

        // 1. 이미지 Base64 인코딩
        String base64BgImage = encodeImageToBase64(BACKGROUND_IMAGE_BASE_PATH + backgroundName + ".png", "image/png");
        String base64CharImage = encodeImageToBase64(CHARACTER_IMAGE_BASE_PATH + dir + lang + ".png", "image/png");

        if (base64BgImage == null || base64CharImage == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "imageName", "Background or character image not found/loadable.");
        }

        int svgWidth = 150;
        int svgHeight = 100;
        int charWidth = 60;
        int charHeight = 60;

        // 캐릭터 초기 위치 (SVG 중앙 하단)
        int initialCharX = (svgWidth - charWidth) / 2;
        int initialCharY = (svgHeight - charHeight) - 8;

        // SVG XML 구성
        StringBuilder svgBuilder = new StringBuilder();
        svgBuilder.append("<svg width=\"").append(svgWidth).append("\" height=\"").append(svgHeight).append("\" ")
                .append("xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">\n");

        // 배경 이미지 삽입 (Base64 인코딩)
        svgBuilder.append("  <image xlink:href=\"data:image/png;base64,").append(base64BgImage)
                .append("\" x=\"0\" y=\"0\" width=\"").append(svgWidth).append("\" height=\"").append(svgHeight).append("\" />\n");

        // 캐릭터 이미지 삽입 및 애니메이션 (Base64 인코딩)
        svgBuilder.append("  <image xlink:href=\"data:image/png;base64,").append(base64CharImage)
                .append("\" x=\"").append(initialCharX).append("\" y=\"").append(initialCharY)
                .append("\" width=\"").append(charWidth).append("\" height=\"").append(charHeight).append("\">\n");

        // SMIL 애니메이션: 좌우 왕복 움직임 (x 좌표 변경) - 뚝뚝 끊기게 이동하도록 calcMode="discrete" 추가
        svgBuilder.append("<animate attributeName=\"x\" values=\"")
                .append(initialCharX).append(";") // 0초: 중앙 (시작)
                .append(initialCharX - CHARACTER_HORIZONTAL_MOVEMENT_PIXELS).append(";") // 0.1초: 왼쪽 1
                .append(initialCharX - CHARACTER_HORIZONTAL_MOVEMENT_PIXELS * 2).append(";") // 0.2초: 왼쪽 2
                .append(initialCharX - CHARACTER_HORIZONTAL_MOVEMENT_PIXELS).append(";") // 0.3초:
                .append(initialCharX).append(";") // 0.4초: 중앙 (오른쪽으로 이동 중 중앙 지점)
                .append(initialCharX + CHARACTER_HORIZONTAL_MOVEMENT_PIXELS).append(";") // 0.5초: 오른쪽 1
                .append(initialCharX + CHARACTER_HORIZONTAL_MOVEMENT_PIXELS * 2).append(";") // 0.6초: 오른쪽 2
                .append(initialCharX + CHARACTER_HORIZONTAL_MOVEMENT_PIXELS).append(";")
                .append(initialCharX).append("\"") // 0.8초: 중앙 (왼쪽으로 이동 중 중앙 지점, 애니메이션 사이클 종료)
                .append(" keyTimes=\"0;0.1;0.2;0.3;0.4;0.5;0.6;0.7;0.8\"") // 키타임 조정 (총 9개 값, 0.1초 간격으로 설정)
                .append(" dur=\"").append(X_ANIMATION_DURATION).append("\"") // 전체 애니메이션 지속 시간 (4초 유지)
                .append(" repeatCount=\"").append(ANIMATION_REPEAT_COUNT).append("\"")
                .append(" calcMode=\"discrete\"/>");

        svgBuilder.append("  </image>\n");

        svgBuilder.append("</svg>");

        return svgBuilder.toString();
    }

    // 이미지 파일을 Base64 문자열로 인코딩하는 헬퍼 메서드
    private String encodeImageToBase64(String imagePath, String mimeType) {
        try {
            InputStream inputStream = resourceLoader.getResource("classpath:" + imagePath).getInputStream();
            byte[] imageBytes = FileCopyUtils.copyToByteArray(inputStream);
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            log.error("Error loading image: {} - {}", imagePath, e.getMessage());
            return null;
        }
    }
}
