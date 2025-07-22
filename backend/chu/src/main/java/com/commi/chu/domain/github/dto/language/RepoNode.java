package com.commi.chu.domain.github.dto.language;

import lombok.Data;

@Data
public class RepoNode {
    /** 레포지토리 내 언어별 사용량 연결 정보 */
    private LanguageConnection languages;
}
