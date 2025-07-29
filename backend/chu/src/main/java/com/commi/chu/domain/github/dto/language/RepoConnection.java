package com.commi.chu.domain.github.dto.language;

import com.commi.chu.domain.github.dto.graphql.PageInfo;
import lombok.Data;

import java.util.List;

/**
 * GraphQL 의 Connection 패턴 중 repositories 에 해당합니다.
 */
@Data
public class RepoConnection {
    /** 다음 페이지 여부와 커서 정보 */
    private PageInfo pageInfo;
    /** 실제 레포지토리 목록 */
    private List<RepoNode> nodes;
}
