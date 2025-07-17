package com.commi.chu.domain.github.service;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.commi.chu.domain.github.dto.response.graphQL.GithubStat;
import com.commi.chu.domain.github.dto.response.graphQL.GraphQlResponse;
import com.commi.chu.domain.github.dto.response.graphQL.UserData;

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
			.body(new ParameterizedTypeReference<GraphQlResponse<GithubStat>>() {
			});
	}
}
