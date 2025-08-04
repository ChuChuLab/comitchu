package com.commi.chu.domain.chu.service;

import com.commi.chu.domain.chu.entity.Chu;
import com.commi.chu.domain.chu.entity.ChuStatus;
import com.commi.chu.domain.chu.repository.ChuRepository;
import com.commi.chu.domain.github.entity.ActivitySnapshotLog;
import com.commi.chu.domain.github.repository.LogRepository;
import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;
import com.commi.chu.global.exception.CustomException;
import com.commi.chu.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChuService {

    private final UserRepository userRepository;
    private final ChuRepository chuRepository;
    private final BadgeGeneratorService badgeGeneratorService;
    private final LogRepository logRepository;

    private static final String BACKGROUND_IMAGE_BASE_PATH = "images/backgrounds/";
    private static final String CHARACTER_IMAGE_BASE_PATH = "images/chu/";

    public String generateCommitchuLevelBadge(String githubUsername) {

        User user = userRepository.findByGithubUsername(githubUsername)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "githubUsername", githubUsername));
        Chu chu = chuRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.CHU_NOT_FOUND));

        String backgroundName = chu.getBackground();
        String lang = chu.getLang();

        return badgeGeneratorService.generateSvgBadge(githubUsername, backgroundName, lang);
    }

    /***
     * chu의 상태를 업데이트 하는 메서드
     * @param user 사용자 정보
     * @param chu 사용자의 chu 정보
     */
    @Transactional
    public void updateChuStatus(User user, Chu chu) {
        List<ActivitySnapshotLog> recentLogs = logRepository.findTop3ByUserIdOrderByActivityDateDesc(user.getId());

        int totalCommits = recentLogs.stream()
            .mapToInt(ActivitySnapshotLog::getCommitCount).sum();

        ChuStatus chuStatus;
        if(totalCommits <= 0){
            chuStatus = ChuStatus.HUNGRY;
        } else if(totalCommits < 5){
            chuStatus = ChuStatus.NORMAL;
        } else{
            chuStatus = ChuStatus.HAPPY;
        }

        chu.updateStatus(chuStatus);
    }
}
