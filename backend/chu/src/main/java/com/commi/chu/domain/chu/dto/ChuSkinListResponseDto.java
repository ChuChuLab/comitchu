package com.commi.chu.domain.chu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChuSkinListResponseDto {

	private Integer langId;

	@JsonProperty("isMain")
	private boolean main;

	@JsonProperty("isUnlocked")
	private boolean unlocked;

	public static ChuSkinListResponseDto of(Integer langId, boolean isMain, boolean isUnlocked) {
		return ChuSkinListResponseDto.builder()
			.langId(langId)
			.main(isMain)
			.unlocked(isUnlocked)
			.build();
	}
}
