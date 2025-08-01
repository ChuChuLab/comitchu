package com.commi.chu.domain.chu.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChuSkinListResponseDto {

	private Integer langId;

	private boolean isMain;

	private boolean isUnlocked;
}
