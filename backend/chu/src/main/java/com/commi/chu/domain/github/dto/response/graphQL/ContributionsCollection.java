package com.commi.chu.domain.github.dto.response.graphQL;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContributionsCollection {
	private ContributionCalendar contributionCalendar;
	private CountWrapper pullRequestContributions;
	private CountWrapper issueContributions;
	private CountWrapper pullRequestReviewContributions;
}
