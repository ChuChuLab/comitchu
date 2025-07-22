package com.commi.chu.domain.github.dto.language;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 서비스를 통해 계산된 “언어별 사용량 통계”를 반환하기 위한 DTO.
 */
@Data
@AllArgsConstructor
public class LanguageStatsDto {
    // 언어 이름 (ex. "Java", "Python")
    private String language;
    // 해당 언어로 작성된 누적 바이트 수
    private long bytes;
    // 전체 대비 비율 (0.00 ~ 100.00)
    private double percentage;
}
