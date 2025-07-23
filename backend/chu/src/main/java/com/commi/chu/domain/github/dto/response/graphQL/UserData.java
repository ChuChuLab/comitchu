package com.commi.chu.domain.github.dto.response.graphQL;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
	GitHub GraphQL API 응답 중 'data' 필드 안의 'user' 데이터를 매핑하기 위한 DTO 클래스입니다.
 */
@Data
public class UserData {
	private ContributionsCollection contributionsCollection;
}
