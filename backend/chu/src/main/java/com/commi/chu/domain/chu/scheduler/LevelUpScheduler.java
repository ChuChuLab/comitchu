package com.commi.chu.domain.chu.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.commi.chu.domain.chu.service.LevelUpService;
import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LevelUpScheduler {

	private final LevelUpService levelUpService;
	private final UserRepository userRepository;

	@Scheduled(cron = "0 5 0 * * *", zone = "Asia/Seoul")
	public void levelUpAllUsers() {

		List<User> users = userRepository.findAll();

		for (User user : users) {
			try{
				levelUpService.levelUp(user.getId());
				log.info("levelUp 로직 완료 : user={}", user.getGithubUsername());
			}
			catch(Exception e){
				log.error("levelUp 과정에서 에러 발생 : user={}", user.getGithubUsername());
			}

		}
	}
}
