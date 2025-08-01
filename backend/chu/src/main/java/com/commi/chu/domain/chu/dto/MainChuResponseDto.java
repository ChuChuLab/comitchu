package com.commi.chu.domain.chu.dto;

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
}
