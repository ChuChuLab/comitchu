package com.commi.chu.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RestClientConfig {

	@Bean
	public RestClient githubRestClient(@Value("${github.token}") String githubToken) {

		log.info("github token: {}", githubToken);

		return RestClient.builder()
			.baseUrl("https://api.github.com/graphql")
			.defaultHeader("Authorization", "Bearer "+githubToken)
			.defaultHeader("Accept", "application/json")
			.build();
	}
}
