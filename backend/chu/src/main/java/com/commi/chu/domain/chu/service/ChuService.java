package com.commi.chu.domain.chu.service;

import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;
import com.commi.chu.global.exception.CustomException;
import com.commi.chu.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChuService {

    private UserRepository userRepository;

    private static final String BASE_IMAGE_PATH = "images/c.png"; // 이미지 경로

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
     * 사용자 레벨이 표시된 커밋츄 뱃지 이미지를 생성하여 바이트 배열로 반환합니다.
     *
     * @return 생성된 뱃지 이미지의 바이트 배열
     * @throws CustomException 뱃지 생성 중 오류 발생 시
     */
    public byte[] generateCommitchuLevelBadge(String githubUsername) {
        int userLevel = getUserLevel(githubUsername); // 사용자 레벨 조회

        BufferedImage originalImage;
        try {
            ClassPathResource imageResource = new ClassPathResource(BASE_IMAGE_PATH);
            try (InputStream inputStream = imageResource.getInputStream()) {
                originalImage = ImageIO.read(inputStream);
                if (originalImage == null) {
                    throw new IOException("Failed to read base image: " + BASE_IMAGE_PATH);
                }
            }
        } catch (IOException e) {
            // 이미지 로딩 실패 시 CustomException 던지기
            throw new CustomException(
                    ErrorCode.CHU_GENERATION_FAILED,
                    "reason", "Image loading failed: " + e.getMessage()
            );
        }

        BufferedImage combinedImage = new BufferedImage(
                originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = combinedImage.createGraphics();

        g2d.drawImage(originalImage, 0, 0, null);

        String levelText = "Lv." + userLevel;
        Font font = new Font("Arial", Font.BOLD, 24);
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(levelText);
        int x = 10;
        int y = fm.getAscent() + 10;

        g2d.drawString(levelText, x, y);
        g2d.dispose();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(combinedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            // 이미지 변환 실패 시 CustomException 던지기
            throw new CustomException(
                    ErrorCode.CHU_GENERATION_FAILED,
                    "reason", "Image conversion failed: " + e.getMessage()
            );
        }
    }

}
