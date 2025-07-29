package com.commi.chu.domain.github.dto.language;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LanguageStatListResponse{
    private final List<LanguageStatsDto> languageStats;
}
