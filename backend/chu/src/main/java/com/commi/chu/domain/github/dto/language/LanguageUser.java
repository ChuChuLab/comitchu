package com.commi.chu.domain.github.dto.language;

import lombok.Data;

/**
 * GraphQL `user` 필드에 매핑되는 DTO.
 * repositories 필드를 통해 레포지토리 목록을 페이징 처리합니다.
 */
@Data
public class LanguageUser {
    /**
     * 해당 사용자의 레포지토리 페이지 연결 정보.
     */
    private RepoConnection repositories;
}
