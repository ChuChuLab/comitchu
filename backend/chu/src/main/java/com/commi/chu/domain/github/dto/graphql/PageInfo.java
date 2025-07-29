package com.commi.chu.domain.github.dto.graphql;

import lombok.Data;

/**
 * GraphQL Connection 패턴의 pageInfo 정보를 매핑합니다.
 * hasNextPage: 다음 페이지가 존재하는지 여부
 * endCursor : 다음 페이지 조회 시 사용할 커서 문자열
 */
@Data
public class PageInfo {
    /**
     * 다음 페이지가 존재하면 true, 아니면 false.
     */
    private boolean hasNextPage;

    /**
     * 다음 페이지를 요청할 때 after 인자로 사용할 커서 값.
     */
    private String endCursor;
}
