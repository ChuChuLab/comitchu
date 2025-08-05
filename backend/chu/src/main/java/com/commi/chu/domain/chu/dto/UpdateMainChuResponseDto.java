package com.commi.chu.domain.chu.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateMainChuResponseDto {

	String message;

	public static UpdateMainChuResponseDto of(String message) {
		return UpdateMainChuResponseDto.builder().message(message).build();
	}
}
