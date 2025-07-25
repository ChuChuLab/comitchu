package com.commi.chu.domain.github.dto.language;

import lombok.Builder;
import lombok.Getter;

/**
 * 서비스를 통해 계산된 “언어별 사용량 통계”를 반환하기 위한 DTO.
 */
@Getter
@Builder
public class LanguageStatsDto {
    // 언어 이름 (ex. "Java", "Python")
    private String language;
    // 해당 언어로 작성된 누적 바이트 수
    private long bytes;
    // 전체 대비 비율 (0.00 ~ 100.00)
    private double percentage;

    public static LanguageStatsDto of(String language, long bytes, long totalBytes) {
        double percentage = Math.round(bytes * 10000.0 / totalBytes) / 100.0;
        return new LanguageStatsDto(language, bytes, percentage);
    }
}
