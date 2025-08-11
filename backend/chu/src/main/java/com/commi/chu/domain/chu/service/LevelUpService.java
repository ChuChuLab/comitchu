package com.commi.chu.domain.chu.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commi.chu.domain.chu.entity.Chu;
import com.commi.chu.domain.chu.repository.ChuRepository;
import com.commi.chu.domain.github.entity.ActivitySnapshot;
import com.commi.chu.domain.github.repository.ActivitySnapshotRepository;
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

	private final ActivitySnapshotRepository activitySnapshotRepository;
	private final UserRepository userRepository;
	private final ChuRepository chuRepository;

	private static final ZoneId KST = ZoneId.of("Asia/Seoul");
	private static final ZoneOffset UTC = ZoneOffset.UTC;

	private static final int MAX_LEVEL = 100;

	private static final long A = 50L;

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

		// KST 기준 어제 날짜
		LocalDate target = LocalDate.now(KST).minusDays(1);

		// 이미 어제 처리 했으면 스킵
		if (target.equals(chu.getLastLeveledDateKst())) {
			log.info("이미 처리한 user 입니다. user={}", user.getGithubUsername());
			return;
		}

		// 어제 KST 00:00:00 ~ 23:59:59 범위를 UTC로 변환
		LocalDateTime startUtc = target.atStartOfDay(KST)
			.withZoneSameInstant(UTC)
			.toLocalDateTime();

		LocalDateTime endUtc = target.plusDays(1).atStartOfDay(KST)
			.withZoneSameInstant(UTC)
			.toLocalDateTime();

		log.info("startUtc={}, endUtc={}", startUtc, endUtc);

		//어제 업데이트 된 github 통계 데이터가 있는지 확인 없으면 Null
		ActivitySnapshot snapshot = activitySnapshotRepository.findFirstByUserIdAndCalculatedAtGreaterThanEqualAndCalculatedAtLessThanOrderByCalculatedAtDesc(userId, startUtc, endUtc)
			.orElse(null);

		if (snapshot == null) {
			// 스냅샷 지연/미생성 시 스케줄 실패 방지: 예외 대신 스킵
			log.error("최신 github 통계가 존재하지 않습니다. user={}", user.getGithubUsername());
			return;
		}

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

		//어제 날짜 데이터로 레벨을 계산하기 때문에 어제 날짜로 저장 (다음날 중복 방지)
		chu.markLeveledToday(target);
	}

	private long requiredExp(int n) {
		// ExpRequired(n) = a * n^2
		return A * (long) n * (long) n;
	}
}
