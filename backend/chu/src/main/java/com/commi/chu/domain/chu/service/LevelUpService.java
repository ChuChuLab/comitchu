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

	private static final int A = 50;
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

		int gainedExp =
			latest.getCommitCount() * W_COMMIT
				+ latest.getPrCount()     * W_PR
				+ latest.getIssueCount()  * W_ISSUE
				+ latest.getReviewCount() * W_REVIEW;

		int level = chu.getLevel();
		int exp = chu.getExp()+ gainedExp;

		while(exp >= requiredExp(level+1)){
			exp -= requiredExp(level+1);
			level++;
		}

		chu.levelUp(level,exp);
	}

	private int requiredExp(int n) {
		// ExpRequired(n) = a * n^2
		return A * n * n;
	}
}
