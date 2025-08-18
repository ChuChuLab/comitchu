package com.commi.chu.domain.chu.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateResponseDto {

	String message;

	public static UpdateResponseDto of(String message) {
		return UpdateResponseDto.builder().message(message).build();
	}
}
