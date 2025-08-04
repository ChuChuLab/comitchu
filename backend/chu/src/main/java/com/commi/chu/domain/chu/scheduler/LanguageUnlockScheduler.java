package com.commi.chu.domain.chu.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.commi.chu.domain.chu.service.ChuLanguageUnlockService;
import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LanguageUnlockScheduler {

	private final ChuLanguageUnlockService languageUnlockService;
	private final UserRepository userRepository;

	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	public void dailyUnlockScheduler() {
		List<User> users = userRepository.findAll();

		for(User user : users) {
			try{
				languageUnlockService.unlockNewLanguage(user.getId());
			} catch (Exception e) {
				log.error("User {} 언어 해금 중 오류", user.getGithubUsername(), e);
			}
		}
	}
}
