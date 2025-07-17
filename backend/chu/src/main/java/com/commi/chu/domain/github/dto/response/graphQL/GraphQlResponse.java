package com.commi.chu.domain.github.dto.response.graphQL;

import lombok.Getter;
import lombok.Setter;

/*
	Github GraphQL 응답 데이터를 받기 위한 최상위 DTO
 */
@Getter
@Setter
public class GraphQlResponse<T> {
	private T data;
}
