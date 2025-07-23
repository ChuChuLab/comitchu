package com.commi.chu.domain.github.service;

import java.time.LocalDateTime;
import java.util.Map;

import com.commi.chu.domain.github.entity.ActivitySnapshot;
import com.commi.chu.domain.github.repository.ActivitySnapshotRepository;
import com.commi.chu.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.commi.chu.domain.github.dto.response.graphQL.GithubStat;
import com.commi.chu.domain.github.dto.response.graphQL.GraphQlResponse;

import lombok.RequiredArgsConstructor;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubStatService {

    private final RestClient githubRestClient;
    private final ActivitySnapshotRepository activitySnapshotRepository;

    //github GraphQL에 user의 통계 정보 요청하는 로직
    public GraphQlResponse<GithubStat> fetchStats(String username) {
        String query = """
                	query {
                	user(login: "%s") {
                		contributionsCollection {
                			contributionCalendar { totalContributions }
                			pullRequestContributions { totalCount }
                			issueContributions { totalCount }
                			pullRequestReviewContributions { totalCount }
                		}
                	}
                }
                """.formatted(username);

        return githubRestClient.post()
                .body(Map.of("query", query))
                .retrieve()
                //응답을 지정한 DTO로 역직렬화
                .body(new ParameterizedTypeReference<GraphQlResponse<GithubStat>>() {
                });
    }


    // 한 유저 처리할 때마다 새 트랜잭션 열기
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUserStat(User user) {
        GraphQlResponse<GithubStat> stat = fetchStats(user.getGithubUsername());

        //전체 커밋 수
        Integer commitCount = stat.getData().getUser().getContributionsCollection().getContributionCalendar().getTotalContributions();

        //전체 pr 수
        Integer prCount = stat.getData().getUser().getContributionsCollection().getPullRequestContributions().getTotalCount();

        //전체 이슈 수
        Integer issueCount = stat.getData().getUser().getContributionsCollection().getIssueContributions().getTotalCount();

        //전체 리뷰 수
        Integer reviewCount = stat.getData().getUser().getContributionsCollection().getPullRequestReviewContributions().getTotalCount();

        //해당 유저의 github 통계를 가져온다.
        ActivitySnapshot githubStat = activitySnapshotRepository.findActivitySnapshotByUserId(user.getId())
                .map(existingStat ->
                        //기존의 github 통계를 업데이트한다.
                        existingStat.updateSnapshot(commitCount, prCount, issueCount, reviewCount)
                )
                .orElseGet(() -> {
                    //기존 github 통계가 없다면 새로운 통계를 저장한다.
                    ActivitySnapshot newStat = ActivitySnapshot.builder()
                            .user(user)
                            .commitCount(commitCount)
                            .prCount(prCount)
                            .issueCount(issueCount)
                            .reviewCount(reviewCount)
                            .calculatedAt(LocalDateTime.now())
                            .build();

                    return activitySnapshotRepository.save(newStat);
                });
        log.info("업데이트 완료: {} → {}", user.getGithubUsername(), githubStat);
    }
}
