package com.commi.chu.domain.github.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.commi.chu.domain.github.service.GithubStatService;
import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubStatScheduler {

	private final UserRepository userRepository;
	private final GithubStatService githubStatService;


	/*
		매일 밤 00시 마다 user들의 github 통계를 업데이트
	 */
	@Scheduled(cron = "0 0 0 * * *")
	public void updateAllGithubStats() {
		List<User> users = userRepository.findActiveGithubUsers();

		for (User user : users) {
			try{
				githubStatService.fetchStats(user.getGithubUsername());

				//업데이트 로직 필요


			}catch (Exception e) {
				log.error("해당 유저의 github 통계를 가져오는데 실패했습니다. : {}", user.getGithubId(), e);
			}
		}
	}
}
