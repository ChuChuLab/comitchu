package com.commi.chu.domain.chu.service;

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

@Service
@RequiredArgsConstructor
public class LevelUpService {

	private final ActivitySnapshotRepository activitySnapshotRepository;
	private final UserRepository userRepository;
	private final ChuRepository chuRepository;

	private static final int MAX_LEVEL = 100;


	private static final long A = 50L;

	//레벨업 기준
	private static final int W_COMMIT = 2;
	private static final int W_PR = 8;
	private static final int W_ISSUE = 5;
	private static final int W_REVIEW = 6;

	@Transactional
	public void levelUp(Integer userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND,"userId",userId));

		Chu chu = chuRepository.findByUser(user)
			.orElseThrow(()-> new CustomException(ErrorCode.CHU_NOT_FOUND));

		ActivitySnapshot latest = activitySnapshotRepository.findByUser_Id(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.RESOURCE_NOT_FOUND, "userId",userId));

		long gainedExp =
			(long)latest.getCommitCount() * W_COMMIT
				+ (long)latest.getPrCount()     * W_PR
				+ (long)latest.getIssueCount()  * W_ISSUE
				+ (long)latest.getReviewCount() * W_REVIEW;

		int level = chu.getLevel();
		long exp = chu.getExp()+ gainedExp;

		//100레벨이 최대
		while (level < MAX_LEVEL && exp >= requiredExp(level + 1)) {
			exp -= requiredExp(level + 1);
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
		return A * (long) n * (long) n;
	}
}
