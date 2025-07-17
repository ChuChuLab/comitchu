package com.commi.chu.domain.github.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commi.chu.domain.github.dto.response.graphQL.GithubStat;
import com.commi.chu.domain.github.dto.response.graphQL.GraphQlResponse;
import com.commi.chu.domain.github.dto.response.graphQL.UserData;
import com.commi.chu.domain.github.service.GithubStatService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GithubStatController {

	private final GithubStatService githubStatService;

	@GetMapping("/{username}/stats")
	public ResponseEntity<?> getStats(@PathVariable String username) {
		GraphQlResponse<GithubStat> response = githubStatService.fetchStats(username);

		return ResponseEntity.ok(response);
	}
}
