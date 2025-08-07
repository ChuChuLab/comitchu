package com.commi.chu.domain.chu.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateBackgroundResponseDto {

	String message;

	public static UpdateBackgroundResponseDto of(String message) {
		return UpdateBackgroundResponseDto.builder()
			.message(message)
			.build();
	}
}
