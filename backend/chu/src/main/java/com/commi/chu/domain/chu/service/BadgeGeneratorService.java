package com.commi.chu.domain.chu.service;

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
    private static final String X_ANIMATION_DURATION = "4s";
    private static final String HOP_ANIMATION_DURATION = "1s";
    private static final String ANIMATION_REPEAT_COUNT = "indefinite";
    private static final int CHARACTER_HORIZONTAL_MOVEMENT_PIXELS = 15;
    private static final int CHARACTER_HOP_HEIGHT = 10;

    @Cacheable(value = "badgeSvg", key = "#githubUsername + '_' + #backgroundName + '_' + #lang")
    public String generateSvgBadge(String githubUsername, String backgroundName, String lang) {

        // 1. 이미지 Base64 인코딩
        String base64BgImage = encodeImageToBase64(BACKGROUND_IMAGE_BASE_PATH + backgroundName + ".png", "image/png");
        String base64CharImage = encodeImageToBase64(CHARACTER_IMAGE_BASE_PATH + lang + ".png", "image/png");

        if (base64BgImage == null || base64CharImage == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "imageName", "Background or character image not found/loadable.");
        }

        int svgWidth = 200;
        int svgHeight = 180;
        int charWidth = 150;
        int charHeight = 150;

        // 캐릭터 초기 위치 (SVG 중앙 하단)
        int initialCharX = (svgWidth - charWidth) / 2;
        int initialCharY = (svgHeight - charHeight) - 10; // 하단에서 10픽셀 위

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

        // SMIL 애니메이션 1: 좌우 왕복 움직임 (x 좌표 변경)
        svgBuilder.append("    <animate attributeName=\"x\" values=\"")
                .append(initialCharX).append(";")
                .append(initialCharX + CHARACTER_HORIZONTAL_MOVEMENT_PIXELS).append(";")
                .append(initialCharX).append(";")
                .append(initialCharX - CHARACTER_HORIZONTAL_MOVEMENT_PIXELS).append(";")
                .append(initialCharX).append("\" ")
                .append("keyTimes=\"0;0.25;0.5;0.75;1\" ")
                .append("dur=\"").append(X_ANIMATION_DURATION).append("\" ")
                .append("repeatCount=\"").append(ANIMATION_REPEAT_COUNT).append("\"/>\n");

        // SMIL 애니메이션 2: 폴짝폴짝 뛰는 움직임 (y 좌표 변경)
        svgBuilder.append("    <animate attributeName=\"y\" values=\"")
                .append(initialCharY).append(";")
                .append(initialCharY - CHARACTER_HOP_HEIGHT).append(";")
                .append(initialCharY).append("\" ")
                .append("keyTimes=\"0;0.5;1\" ")
                .append("dur=\"").append(HOP_ANIMATION_DURATION).append("\" ")
                .append("repeatCount=\"").append(ANIMATION_REPEAT_COUNT).append("\"/>\n");

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
