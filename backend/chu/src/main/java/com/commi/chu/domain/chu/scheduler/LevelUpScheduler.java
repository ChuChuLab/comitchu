package com.commi.chu.domain.chu.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.commi.chu.domain.chu.service.LevelUpService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LevelUpScheduler {

	private final LevelUpService levelUpService;

	@Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
	public void levelUp() {

	}
}
