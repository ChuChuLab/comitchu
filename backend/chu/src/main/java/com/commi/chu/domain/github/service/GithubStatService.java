package com.commi.chu.domain.github.service;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.commi.chu.domain.github.dto.response.graphQL.GithubStat;
import com.commi.chu.domain.github.dto.response.graphQL.GraphQlResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GithubStatService {

	private final RestClient githubRestClient;

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
			.body(Map.of("query",query))
			.retrieve()
			//응답을 지정한 DTO로 역직렬화
			.body(new ParameterizedTypeReference<GraphQlResponse<GithubStat>>() {
			});
	}
}
