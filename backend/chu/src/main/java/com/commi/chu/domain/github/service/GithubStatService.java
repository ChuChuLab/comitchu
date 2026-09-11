package com.commi.chu.domain.github.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.commi.chu.domain.chu.entity.Chu;
import com.commi.chu.domain.chu.repository.ChuRepository;
import com.commi.chu.domain.chu.service.ChuService;
import com.commi.chu.domain.github.entity.ActivitySnapshot;
import com.commi.chu.domain.github.entity.ActivitySnapshotLog;
import com.commi.chu.domain.github.repository.ActivitySnapshotRepository;
import com.commi.chu.domain.github.repository.LogRepository;
import com.commi.chu.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.commi.chu.domain.github.dto.statistics.GithubStat;
import com.commi.chu.domain.github.dto.graphql.GraphQlResponse;
import com.commi.chu.global.exception.CustomException;
import com.commi.chu.global.exception.code.ErrorCode;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubStatService {

    private final RestClient githubRestClient;
    private final LogRepository logRepository;
    private final ChuService chuService;
    private final ChuRepository chuRepository;
    private final ActivitySnapshotRepository activitySnapshotRepository;

    /***
     * user의 오늘 하루 단위의 전체 커밋 수, pr 수, 이슈 수, 리뷰 수를 가져오는 통계 api입니다.
     * @param username github 유저 네임
     * @return github GraphQL로 통계 데이터를 받아옵니다.
     */
    public GraphQlResponse<GithubStat> fetchStats(String username) {
		return fetchStats(username, LocalDate.now(ZoneId.of("Asia/Seoul")));
	}

	public GraphQlResponse<GithubStat> fetchStats(String username, LocalDate activityDate) {
        String query = """
                	query($login: String!, $from: DateTime!, $to: DateTime!) {
                	user(login: $login) {
                		contributionsCollection(from: $from, to: $to) {
                			totalCommitContributions
                            totalIssueContributions
                            totalPullRequestContributions
                            totalPullRequestReviewContributions
                		}
                	}
                }
                """;

        Map<String, Object> params = new HashMap<>();

		ZonedDateTime from = activityDate.atStartOfDay(ZoneId.of("Asia/Seoul")).withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime to = from.plusDays(1).minusSeconds(1);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

        String fromStr = formatter.format(from);
        String toStr = formatter.format(to);

        params.put("login", username);
        params.put("from", fromStr);
        params.put("to", toStr);

        return githubRestClient.post()
                .body(Map.of("query", query, "variables", params    ))
                .retrieve()
                //응답을 지정한 DTO로 역직렬화
                .body(new ParameterizedTypeReference<GraphQlResponse<GithubStat>>() {
                });
    }


    /***
     *
     * user의 github 통계를 업데이트하는 함수입니다.
     * @param user 업데이트할 user
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUserStat(User user) {
		LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
		ActivitySnapshotLog snapshotLog = collectActivityForDate(user, today);

		Integer commitCount = snapshotLog.getCommitCount();
		Integer prCount = snapshotLog.getPrCount();
		Integer issueCount = snapshotLog.getIssueCount();
		Integer reviewCount = snapshotLog.getReviewCount();

		activitySnapshotRepository.findByUser_Id(user.getId())
			.map(existingStat -> existingStat.updateSnapshot(commitCount, prCount, issueCount, reviewCount))
			.orElseGet(() -> activitySnapshotRepository.save(ActivitySnapshot.builder()
				.user(user)
				.commitCount(commitCount)
				.prCount(prCount)
				.issueCount(issueCount)
				.reviewCount(reviewCount)
				.calculatedAt(LocalDateTime.now())
				.build()));

		Chu chu = chuRepository.findByUser(user)
			.orElseThrow(() -> new CustomException(ErrorCode.CHU_NOT_FOUND));

		chuService.updateChuStatus(user, chu);

		log.info("업데이트 완료: {} → chu 상태 : {}", user.getGithubUsername(), chu.getStatus());
	}

	/**
	 * 지정한 KST 날짜의 GitHub 활동을 수집한다. 같은 날짜를 다시 수집하면 기존 로그를 갱신한다.
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public ActivitySnapshotLog collectActivityForDate(User user, LocalDate activityDate) {
		// 과거 날짜도 같은 방식으로 조회할 수 있어 스케줄 장애 이후 누락분을 복구할 수 있다.
		GraphQlResponse<GithubStat> stat = fetchStats(user.getGithubUsername(), activityDate);

        //전체 커밋 수
        Integer commitCount = stat.getData().getUser().getContributionsCollection().getTotalCommitContributions();

        //전체 pr 수
        Integer prCount = stat.getData().getUser().getContributionsCollection().getTotalPullRequestContributions();

        //전체 이슈 수
        Integer issueCount = stat.getData().getUser().getContributionsCollection().getTotalIssueContributions();

        //전체 리뷰 수
        Integer reviewCount = stat.getData().getUser().getContributionsCollection().getTotalPullRequestReviewContributions();

        //해당 유저의 github 통계를 가져온다.
		// 같은 날짜를 재수집할 때 로그를 추가하지 않고 기존 값을 갱신한다.
		ActivitySnapshotLog snapshotLog = logRepository
			.findFirstByUserIdAndActivityDateOrderByCreatedAtDesc(user.getId(), activityDate)
			.orElseGet(() -> ActivitySnapshotLog.builder()
				.user(user)
				.activityDate(activityDate)
				.commitCount(commitCount)
				.prCount(prCount)
				.issueCount(issueCount)
				.reviewCount(reviewCount)
				.build());

		snapshotLog.updateCounts(commitCount, prCount, issueCount, reviewCount);
		return logRepository.save(snapshotLog);
	}
}
