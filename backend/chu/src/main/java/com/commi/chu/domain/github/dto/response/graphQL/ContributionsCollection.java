package com.commi.chu.domain.github.dto.response.graphQL;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
	contributionsCollection은 커밋, PR, 이슈, 리뷰 등의 기여 정보를 포함합니다.
 */
@Data
public class ContributionsCollection {

	//전체 커밋 수
	private ContributionCalendar contributionCalendar;

	//전체 pr 수
	private CountWrapper pullRequestContributions;

	//전체 이슈 수
	private CountWrapper issueContributions;

	//전제 pr 리뷰 수
	private CountWrapper pullRequestReviewContributions;
}
