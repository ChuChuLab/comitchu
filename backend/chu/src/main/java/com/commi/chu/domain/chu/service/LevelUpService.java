package com.commi.chu.domain.chu.service;

import java.time.LocalDate;
import java.time.ZoneId;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commi.chu.domain.chu.entity.Chu;
import com.commi.chu.domain.chu.repository.ChuRepository;
import com.commi.chu.domain.github.entity.ActivitySnapshotLog;
import com.commi.chu.domain.github.repository.LogRepository;
import com.commi.chu.domain.github.service.GithubStatService;
import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;
import com.commi.chu.global.exception.CustomException;
import com.commi.chu.global.exception.code.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LevelUpService {

	private final LogRepository logRepository;
	private final GithubStatService githubStatService;
	private final UserRepository userRepository;
	private final ChuRepository chuRepository;

	private static final ZoneId KST = ZoneId.of("Asia/Seoul");
	private static final int MAX_LEVEL = 100;

	//레벨업 기준
	private static final int W_COMMIT = 2;
	private static final int W_PR = 8;
	private static final int W_ISSUE = 5;
	private static final int W_REVIEW = 6;

	@Transactional
	public void levelUp(Integer userId) {
		log.info("levelUp start");

		User user = userRepository.findById(userId)
			.orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND,"userId",userId));

		Chu chu = chuRepository.findByUser(user)
			.orElseThrow(()-> new CustomException(ErrorCode.CHU_NOT_FOUND));

		LocalDate yesterday = LocalDate.now(KST).minusDays(1);
		LocalDate target = chu.getLastLeveledDateKst() == null
			? yesterday
			: chu.getLastLeveledDateKst().plusDays(1);

		if (target.isAfter(yesterday)) {
			log.info("이미 처리한 user 입니다. user={}", user.getGithubUsername());
			return;
		}

		while (!target.isAfter(yesterday)) {
			ActivitySnapshotLog snapshot = getOrCollectActivity(user, target);
			if (snapshot == null) return;

			applyExperience(chu, snapshot);
			chu.markLeveledToday(target);
			target = target.plusDays(1);
		}
	}

	private ActivitySnapshotLog getOrCollectActivity(User user, LocalDate target) {
		return logRepository.findFirstByUserIdAndActivityDateOrderByCreatedAtDesc(user.getId(), target)
			.orElseGet(() -> {
				try {
					log.info("누락된 GitHub 통계를 재수집합니다. user={}, date={}", user.getGithubUsername(), target);
					return githubStatService.collectActivityForDate(user, target);
				} catch (Exception e) {
					log.error("GitHub 통계 재수집 실패. user={}, date={}", user.getGithubUsername(), target, e);
					return null;
				}
			});
	}

	private void applyExperience(Chu chu, ActivitySnapshotLog snapshot) {

		long gainedExp =
			(long)snapshot.getCommitCount() * W_COMMIT
				+ (long)snapshot.getPrCount()     * W_PR
				+ (long)snapshot.getIssueCount()  * W_ISSUE
				+ (long)snapshot.getReviewCount() * W_REVIEW;

		int level = chu.getLevel();
		long exp = chu.getExp()+ gainedExp;

		log.info("levelUp exp={}", exp);
		log.info("level={}", level);

		//100레벨이 최대
		while (level < MAX_LEVEL) {
			long need = requiredExp(level + 1);
			if (exp < need) break;
			exp -= need;
			level++;
		}

		//만렙이면 경험치는 0으로 고정
		if (level >= MAX_LEVEL) {
			level = MAX_LEVEL;
			exp   = 0L;
		}

		chu.levelUp(level, (int) exp);

	}

	private long requiredExp(int n) {
		// ExpRequired(n) = a * n^2
		if(n<=10) return 2L * (long) n * (long) n;
		if(n<=40) return 8L * (long) n * (long) n;
		return 20L * (long) n * (long) n;
	}
}
