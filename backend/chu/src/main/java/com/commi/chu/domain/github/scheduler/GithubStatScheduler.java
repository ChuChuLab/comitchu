package com.commi.chu.domain.github.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import com.commi.chu.domain.github.dto.response.graphQL.GithubStat;
import com.commi.chu.domain.github.dto.response.graphQL.GraphQlResponse;
import com.commi.chu.domain.github.entity.ActivitySnapshot;
import com.commi.chu.domain.github.repository.ActivitySnapshotRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.commi.chu.domain.github.service.GithubStatService;
import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubStatScheduler {

    private final UserRepository userRepository;
    private final GithubStatService githubStatService;


    /*
        매일 밤 00시 마다 user들의 github 통계를 업데이트
     */
    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public void updateAllGithubStats() {
        List<User> users = userRepository.findActiveGithubUsers();

        for (User user : users) {
            try {
                githubStatService.updateUserStat(user);
                log.info("GitHub 통계 업데이트 완료: user={}", user.getGithubUsername());

            } catch (Exception e) {
                log.error("해당 유저의 github 통계 업데이트 실패: userGithubUsername={}", e.getMessage());
            }
        }
    }


}
