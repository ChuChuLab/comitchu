package com.commi.chu.domain.github.dto.graphql;

import lombok.Data;

/*
	Github GraphQL 응답 데이터를 받기 위한 최상위 DTO
 */
@Data
public class GraphQlResponse<T> {
	private T data;
}
