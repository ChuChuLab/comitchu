package com.commi.chu.domain.chu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.commi.chu.domain.chu.entity.Chu;
import com.commi.chu.domain.chu.repository.ChuRepository;
import com.commi.chu.domain.github.entity.ActivitySnapshotLog;
import com.commi.chu.domain.github.repository.LogRepository;
import com.commi.chu.domain.github.service.GithubStatService;
import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class LevelUpServiceTest {

	private static final ZoneId KST = ZoneId.of("Asia/Seoul");

	@Mock
	private LogRepository logRepository;
	@Mock
	private GithubStatService githubStatService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private ChuRepository chuRepository;

	@InjectMocks
	private LevelUpService levelUpService;

	@Test
	void missingActivityIsCollectedAndApplied() {
		LocalDate yesterday = LocalDate.now(KST).minusDays(1);
		User user = user();
		Chu chu = chu(null);
		ActivitySnapshotLog recovered = activity(user, yesterday, 3);

		given(userRepository.findById(1)).willReturn(Optional.of(user));
		given(chuRepository.findByUser(user)).willReturn(Optional.of(chu));
		given(logRepository.findFirstByUserIdAndActivityDateOrderByCreatedAtDesc(1, yesterday))
			.willReturn(Optional.empty());
		given(githubStatService.collectActivityForDate(user, yesterday)).willReturn(recovered);

		levelUpService.levelUp(1);

		assertEquals(1, chu.getLevel());
		assertEquals(6, chu.getExp());
		assertEquals(yesterday, chu.getLastLeveledDateKst());
	}

	@Test
	void collectionFailureLeavesDatePendingForNextRun() {
		LocalDate yesterday = LocalDate.now(KST).minusDays(1);
		User user = user();
		Chu chu = chu(null);

		given(userRepository.findById(1)).willReturn(Optional.of(user));
		given(chuRepository.findByUser(user)).willReturn(Optional.of(chu));
		given(logRepository.findFirstByUserIdAndActivityDateOrderByCreatedAtDesc(1, yesterday))
			.willReturn(Optional.empty());
		given(githubStatService.collectActivityForDate(user, yesterday))
			.willThrow(new IllegalStateException("GitHub unavailable"));

		levelUpService.levelUp(1);

		assertEquals(0, chu.getExp());
		assertNull(chu.getLastLeveledDateKst());
	}

	@Test
	void pendingDatesAreAppliedInOrder() {
		LocalDate yesterday = LocalDate.now(KST).minusDays(1);
		LocalDate firstPendingDate = yesterday.minusDays(1);
		User user = user();
		Chu chu = chu(firstPendingDate.minusDays(1));
		ActivitySnapshotLog first = activity(user, firstPendingDate, 1);
		ActivitySnapshotLog second = activity(user, yesterday, 2);

		given(userRepository.findById(1)).willReturn(Optional.of(user));
		given(chuRepository.findByUser(user)).willReturn(Optional.of(chu));
		given(logRepository.findFirstByUserIdAndActivityDateOrderByCreatedAtDesc(1, firstPendingDate))
			.willReturn(Optional.of(first));
		given(logRepository.findFirstByUserIdAndActivityDateOrderByCreatedAtDesc(1, yesterday))
			.willReturn(Optional.of(second));

		levelUpService.levelUp(1);

		assertEquals(6, chu.getExp());
		assertEquals(yesterday, chu.getLastLeveledDateKst());
		then(githubStatService).should(never()).collectActivityForDate(user, firstPendingDate);
		then(githubStatService).should(never()).collectActivityForDate(user, yesterday);
	}

	private User user() {
		return User.builder().id(1).githubUsername("tester").build();
	}

	private Chu chu(LocalDate lastLeveledDate) {
		return Chu.builder()
			.level(1)
			.exp(0)
			.lastLeveledDateKst(lastLeveledDate)
			.build();
	}

	private ActivitySnapshotLog activity(User user, LocalDate date, int commits) {
		return ActivitySnapshotLog.builder()
			.user(user)
			.activityDate(date)
			.commitCount(commits)
			.prCount(0)
			.issueCount(0)
			.reviewCount(0)
			.build();
	}
}
