package com.commi.chu.domain.github.dto.statistics;

import com.commi.chu.domain.github.dto.graphql.UserData;
import lombok.Data;

/*
	GitHub 통계를 얻기 위한 dto
 */
@Data
public class GithubStat {
	private UserData user;
}
