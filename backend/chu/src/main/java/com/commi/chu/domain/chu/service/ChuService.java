package com.commi.chu.domain.chu.service;

import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;
import com.commi.chu.global.exception.CustomException;
import com.commi.chu.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChuService {

    private UserRepository userRepository;

    private static final String BACKGROUND_IMAGE_BASE_PATH = "images/backgrounds/";
    private static final String CHARACTER_IMAGE_BASE_PATH = "images/chu/";

    // --- 애니메이션 관련 상수 ---
    private static final String X_ANIMATION_DURATION = "4s"; // 좌우 왕복 애니메이션 주기 (좀 더 길게)
    private static final String HOP_ANIMATION_DURATION = "1s"; // 폴짝거리는 애니메이션 주기
    private static final String ANIMATION_REPEAT_COUNT = "indefinite"; // 무한 반복
    private static final int CHARACTER_HORIZONTAL_MOVEMENT_PIXELS = 15; // 캐릭터가 최대로 좌우로 움직일 픽셀
    private static final int CHARACTER_HOP_HEIGHT = 10; // 캐릭터가 위로 뛸 높이 픽셀

    public String generateCommitchuLevelBadge(String githubUsername, String backgroundName, String characterName) {

        // 1. 이미지 Base64 인코딩
        String base64BgImage = encodeImageToBase64(BACKGROUND_IMAGE_BASE_PATH + backgroundName, "image/png");
        String base64CharImage = encodeImageToBase64(CHARACTER_IMAGE_BASE_PATH + characterName, "image/png");

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

        // SVG XML 구성 (StringBuilder 사용 권장)
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
                .append(initialCharX).append(";") // 시작 위치
                .append(initialCharX + CHARACTER_HORIZONTAL_MOVEMENT_PIXELS).append(";") // 오른쪽으로 이동
                .append(initialCharX).append(";") // 다시 가운데
                .append(initialCharX - CHARACTER_HORIZONTAL_MOVEMENT_PIXELS).append(";") // 왼쪽으로 이동
                .append(initialCharX).append("\" ") // 다시 시작 위치
                .append("keyTimes=\"0;0.25;0.5;0.75;1\" ") // 각 지점의 시간 비율
                .append("dur=\"").append(X_ANIMATION_DURATION).append("\" ") // 좌우 움직임 주기
                .append("repeatCount=\"").append(ANIMATION_REPEAT_COUNT).append("\"/>\n");

        // SMIL 애니메이션 2: 폴짝폴짝 뛰는 움직임 (y 좌표 변경)
        svgBuilder.append("    <animate attributeName=\"y\" values=\"")
                .append(initialCharY).append(";") // 시작 (아래)
                .append(initialCharY - CHARACTER_HOP_HEIGHT).append(";") // 위로 점프
                .append(initialCharY).append("\" ") // 다시 아래
                .append("keyTimes=\"0;0.5;1\" ") // 중간에 정점
                .append("dur=\"").append(HOP_ANIMATION_DURATION).append("\" ") // 폴짝거리는 주기
                .append("repeatCount=\"").append(ANIMATION_REPEAT_COUNT).append("\"/>\n");

        svgBuilder.append("  </image>\n");

        svgBuilder.append("</svg>");

        return svgBuilder.toString();
    }

    /**
     * 이미지 파일을 Base64 문자열로 인코딩합니다.
     * @param path 이미지 리소스 경로
     * @param mimeType 이미지 MIME 타입 (예: "image/png")
     * @return Base64 인코딩된 문자열 또는 null (로딩 실패 시)
     */
    private String encodeImageToBase64(String path, String mimeType) {
        try {
            ClassPathResource imageResource = new ClassPathResource(path);
            if (!imageResource.exists()) {
                log.warn("Image file not found for Base64 encoding: {}", path);
                return null;
            }
            try (InputStream inputStream = imageResource.getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                byte[] imageBytes = outputStream.toByteArray();
                return Base64.getEncoder().encodeToString(imageBytes);
            }
        } catch (IOException e) {
            log.error("IO error encoding image {} to Base64: {}", path, e.getMessage());
            return null;
        }
    }

}
