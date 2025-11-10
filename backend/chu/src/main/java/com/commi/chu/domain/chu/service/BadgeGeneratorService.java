package com.commi.chu.domain.chu.service;

import com.commi.chu.global.exception.CustomException;
import com.commi.chu.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BadgeGeneratorService {

    private final ResourceLoader resourceLoader;

    private static final String BACKGROUND_IMAGE_BASE_PATH = "images/backgrounds/";
    private static final String CHARACTER_IMAGE_BASE_PATH  = "images/chu/";

    // --- 애니메이션 관련 상수 ---
    private static final String X_ANIMATION_DURATION = "6s";
    private static final String ANIMATION_REPEAT_COUNT = "indefinite";
    private static final int CHARACTER_HORIZONTAL_MOVEMENT_PIXELS = 15;

    @Cacheable(value = "badgeSvg", key = "#githubUsername + '_' + #backgroundName + '_' + #lang + '_' + #status")
    public String generateSvgBadge(String githubUsername, String backgroundName, String lang, String status) {

        String dir = "normal/";
        if ("HUNGRY".equals(status))      dir = "hungry/";
        else if ("HAPPY".equals(status))  dir = "happy/";

        // 1) SVG 파일을 그대로 읽어서 <svg> 바디만 추출
        String bgInner   = loadSvgInner(BACKGROUND_IMAGE_BASE_PATH + backgroundName + ".svg");
        String charInner = loadSvgInner(CHARACTER_IMAGE_BASE_PATH  + dir + lang + ".svg");

        if (bgInner == null || charInner == null) {
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "imageName", "Background or character SVG not found/loadable.");
        }

        // 배지/캐릭터 크기
        int svgWidth = 150;
        int svgHeight = 100;
        int charWidth = 60;
        int charHeight = 60;

        // 캐릭터 초기 위치 (중앙 하단)
        int initialCharX = (svgWidth - charWidth) / 2;
        int initialCharY = (svgHeight - charHeight) - 8;

        // 애니메이션 경로(translate) 구성
        int L1 = initialCharX - CHARACTER_HORIZONTAL_MOVEMENT_PIXELS;
        int L2 = initialCharX - CHARACTER_HORIZONTAL_MOVEMENT_PIXELS * 2;
        int R1 = initialCharX + CHARACTER_HORIZONTAL_MOVEMENT_PIXELS;
        int R2 = initialCharX + CHARACTER_HORIZONTAL_MOVEMENT_PIXELS * 2;
        String translateValues =
                initialCharX + "," + initialCharY + ";" +   // center
                        L1           + "," + initialCharY + ";" +
                        L2           + "," + initialCharY + ";" +
                        L1           + "," + initialCharY + ";" +
                        initialCharX + "," + initialCharY + ";" +
                        R1           + "," + initialCharY + ";" +
                        R2           + "," + initialCharY + ";" +
                        R1           + "," + initialCharY + ";" +
                        initialCharX + "," + initialCharY;

        StringBuilder svg = new StringBuilder();
        svg.append("<svg viewBox=\"0 0 ").append(svgWidth).append(" ").append(svgHeight).append("\" ")
                .append("xmlns=\"http://www.w3.org/2000/svg\" width=\"100%\" height=\"100%\">\n");

        // 스타일
        svg.append("  <style>")
                .append("  *{shape-rendering:crispEdges}")
                .append("  </style>\n");

        // defs: 읽어온 SVG들을 symbol로 보관
        svg.append("  <defs>\n")
                .append("    <clipPath id=\"roundedBg\"><rect x=\"0\" y=\"0\" width=\"100%\" height=\"100%\" rx=\"8\" ry=\"8\"/></clipPath>\n")
                .append("    <symbol id=\"bg\" viewBox=\"0 0 ").append(svgWidth).append(" ").append(svgHeight).append("\">")
                .append(bgInner)
                .append("</symbol>\n")
                .append("    <symbol id=\"char\" viewBox=\"0 0 ").append(charWidth).append(" ").append(charHeight).append("\">")
                .append(charInner)
                .append("</symbol>\n")
                .append("  </defs>\n");

        // 배경
        svg.append("  <use href=\"#bg\" x=\"0\" y=\"0\" width=\"100%\" height=\"100%\" clip-path=\"url(#roundedBg)\"/>\n");

        // 캐릭터 + 애니메이션 (translate로 좌우 왕복)
        svg.append("  <g id=\"charWrap\" transform=\"translate(")
                .append(initialCharX).append(",").append(initialCharY).append(")\">\n")
                .append("    <use href=\"#char\" width=\"").append(charWidth).append("\" height=\"").append(charHeight).append("\"/>\n")
                .append("    <animateTransform attributeName=\"transform\" type=\"translate\" ")
                .append("values=\"").append(translateValues).append("\" ")
                .append("keyTimes=\"0;0.1;0.2;0.3;0.4;0.5;0.6;0.7;0.8\" ")
                .append("dur=\"").append(X_ANIMATION_DURATION).append("\" repeatCount=\"").append(ANIMATION_REPEAT_COUNT).append("\" ")
                .append("calcMode=\"discrete\"/>\n")
                .append("  </g>\n");

        // 테두리
        svg.append("  <rect x=\"0\" y=\"0\" width=\"100%\" height=\"100%\" rx=\"8\" ry=\"8\" fill=\"none\" stroke=\"#00000030\" stroke-width=\"1\"/>\n");

        svg.append("</svg>");
        return svg.toString();
    }

    /** classpath의 SVG 파일을 읽어 <svg> 태그 안쪽(컨텐츠)만 반환 */
    private String loadSvgInner(String classpathSvg) {
        try (InputStream in = resourceLoader.getResource("classpath:" + classpathSvg).getInputStream()) {
            String raw = new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            // <svg ...>와 </svg> 제거
            raw = raw.replaceFirst("(?is)^.*?<svg[^>]*>", "");
            raw = raw.replaceFirst("(?is)</svg>\\s*$", "");
            return raw;
        } catch (IOException e) {
            log.error("Error loading svg: {} - {}", classpathSvg, e.getMessage());
            return null;
        }
    }
}
