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

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChuService {

    private UserRepository userRepository;

    private static final String BACKGROUND_IMAGE_BASE_PATH = "images/backgrounds/";
    private static final String CHARACTER_IMAGE_BASE_PATH = "images/chu/";
    // 폰트 설정 상수화
    private static final String DEFAULT_FONT_NAME = "Arial";
    private static final int DEFAULT_FONT_STYLE = Font.BOLD;
    private static final int DEFAULT_FONT_SIZE = 24; // 글씨 크기 기본값
    private static final Color DEFAULT_TEXT_COLOR = Color.BLACK;

    /**
     * test를 위한 코드입니다.
     * @return 5000 (만렙)
     */
    public int getUserLevel(String githubUsername) {
        // 실제로는 DB에서 조회하거나 외부 API를 호출하는 로직이 들어갑니다.

        // 테스트 단계에서는 일단 주석처리
//        User user = userRepository.findByGithubUsername(githubUsername)
//                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "username", githubUsername));

        return 5000;
    }

    /**
     * 지정된 배경과 캐릭터를 사용하여 사용자 레벨이 표시된 커밋츄 뱃지 이미지를 생성하여 바이트 배열로 반환합니다.
     *
     * @param githubUsername 사용자 GitHub ID
     * @param backgroundName 배경 이미지 파일명 (예: "forest.png")
     * @param characterName 캐릭터 이미지 파일명 (예: "pikachu.png")
     * @return 생성된 뱃지 이미지의 바이트 배열
     * @throws CustomException 뱃지 생성 중 오류 발생 시
     */
    public byte[] generateCommitchuLevelBadge(String githubUsername, String backgroundName, String characterName) {
        int userLevel = getUserLevel(githubUsername); // 사용자 레벨 조회

        // 1. 배경 이미지 로드
        BufferedImage backgroundImage = loadImage(BACKGROUND_IMAGE_BASE_PATH + backgroundName);
        if (backgroundImage == null) {
            log.error("Background image not found or failed to load: {}", BACKGROUND_IMAGE_BASE_PATH + backgroundName);
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "backgroundName", backgroundName);
        }

        // 2. 캐릭터 이미지 로드
        BufferedImage characterImage = loadImage(CHARACTER_IMAGE_BASE_PATH + characterName);
        if (characterImage == null) {
            log.error("Character image not found or failed to load: {}", CHARACTER_IMAGE_BASE_PATH + characterName);
            throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "characterName", characterName);
        }

        // 3. 합성할 이미지 생성 (배경 이미지 크기 기준, 투명도 지원)
        BufferedImage combinedImage = new BufferedImage(
                backgroundImage.getWidth(), backgroundImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = combinedImage.createGraphics();

        // 앤티앨리어싱 설정 (이미지와 텍스트 모두 부드럽게)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 4. 배경 이미지 그리기
        g2d.drawImage(backgroundImage, 0, 0, null);

        // 5. 캐릭터 이미지 그리기 (배경 이미지 위에)
        // 캐릭터의 위치와 크기를 조절해야 합니다. 여기서는 예시로 가운데 정렬
        int charX = (backgroundImage.getWidth() - characterImage.getWidth()) / 2;
        int charY = (backgroundImage.getHeight() - characterImage.getHeight()) / 2;
        g2d.drawImage(characterImage, charX, charY, characterImage.getWidth(), characterImage.getHeight(), null);

        // 6. 레벨 텍스트 설정 및 그리기
        String levelText = "Lv." + userLevel;
        Font font = new Font(DEFAULT_FONT_NAME, DEFAULT_FONT_STYLE, DEFAULT_FONT_SIZE);
        g2d.setFont(font);
        g2d.setColor(DEFAULT_TEXT_COLOR);

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(levelText);
        int textX = (combinedImage.getWidth() - textWidth) / 2;
        int textY = fm.getAscent() + 10;

        g2d.drawString(levelText, textX, textY);

        // 7. Graphics2D 자원 해제
        g2d.dispose();

        // 8. 완성된 이미지를 PNG 바이트 배열로 변환
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(combinedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("Error converting combined image to byte array: {}", e.getMessage(), e);
            throw new CustomException(
                    ErrorCode.CHU_GENERATION_FAILED,
                    "reason", "Image conversion failed: " + e.getMessage()
            );
        }
    }

    /**
     * 주어진 경로에서 이미지를 로드합니다. 실패 시 null 반환.
     */
    private BufferedImage loadImage(String path) {
        try {
            ClassPathResource imageResource = new ClassPathResource(path);
            if (!imageResource.exists()) {
                log.warn("Image file not found: {}", path);
                return null;
            }
            try (InputStream inputStream = imageResource.getInputStream()) {
                BufferedImage image = ImageIO.read(inputStream);
                if (image == null) {
                    log.error("Failed to read image content from: {}", path);
                }
                return image;
            }
        } catch (IOException e) {
            log.error("IO error loading image {}: {}", path, e.getMessage());
            return null;
        }
    }

}
