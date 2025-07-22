package com.commi.chu.domain.github.dto.language;

import lombok.Data;

/**
 * GraphQlResponse.data 필드의 최상위 래퍼로,
 * user 정보가 포함됩니다.
 */
@Data
public class LanguageDataWrapper {
    /**
     * 사용자 정보와 해당 사용자의 레포지토리 연결 정보가 담긴 객체.
     */
    private LanguageUser user;
}
