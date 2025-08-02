package com.commi.chu.domain.chu.dto;

import com.commi.chu.domain.chu.entity.Chu;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainChuResponseDto {

	private String nickname;

	private int level;

	private int exp;

	private String status;

	private String lang;

	private String background;

	public static MainChuResponseDto of(Chu chu) {
		return MainChuResponseDto.builder()
			.nickname(chu.getName())
			.level(chu.getLevel())
			.exp(chu.getExp())
			.status(chu.getStatus().toString())
			.lang(chu.getLang())
			.background(chu.getBackground())
			.build();
	}
}
