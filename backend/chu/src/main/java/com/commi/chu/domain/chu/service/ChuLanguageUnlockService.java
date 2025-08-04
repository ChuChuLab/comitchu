package com.commi.chu.domain.chu.service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.commi.chu.domain.chu.entity.Chu;
import com.commi.chu.domain.chu.entity.Language;
import com.commi.chu.domain.chu.entity.UserLang;
import com.commi.chu.domain.chu.repository.ChuRepository;
import com.commi.chu.domain.chu.repository.LanguageRepository;
import com.commi.chu.domain.chu.repository.UserLangRepository;
import com.commi.chu.domain.github.dto.language.LanguageStatsDto;
import com.commi.chu.domain.github.service.GithubLanguageService;
import com.commi.chu.domain.user.entity.User;
import com.commi.chu.domain.user.repository.UserRepository;
import com.commi.chu.global.exception.CustomException;
import com.commi.chu.global.exception.code.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChuLanguageUnlockService {

	private final LanguageRepository languageRepository;
	private final UserLangRepository userLangRepository;
	private final UserRepository userRepository;
	private final ChuRepository chuRepository;

	private final GithubLanguageService githubLanguageService;

	@Transactional
	public void unlockNewLanguage(Integer userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND,"userId",userId));

		//이미 해방된 언어 id
		Set<Integer> unlockLangIds = userLangRepository.findByUserId(user.getId())
			.stream()
			.map(userLang -> userLang.getLang().getId())
			.collect(Collectors.toSet());

		log.info("unlockNewLanguage:{}", unlockLangIds);

		//잠긴 언어 리스트
		Set<String> lockedLangNames = languageRepository.findAll()
			.stream()
			.filter(language -> !unlockLangIds.contains(language.getId()))
			.map(Language::getLang)
			.collect(Collectors.toSet());

		log.info("lockNewLanguage:{}", lockedLangNames.toString());

		//전체 언어통계를 가져와 해금
		List<LanguageStatsDto> stats = githubLanguageService.getAllLanguageStats(userId);

		//해금 안된 언어 중에 새롭게 해금해야될 언어들
		Set<String> newUnlockLangNames = stats.stream()
			.map(LanguageStatsDto::getLanguage)
			.filter(lockedLangNames::contains)
			.collect(Collectors.toSet());

		//엔티티 생성 & 일괄 저장
		List<UserLang> toUnlock = newUnlockLangNames.stream()
			.map(langName -> languageRepository.findByLang(langName).orElse(null))
			.filter(Objects::nonNull)
			.map(lang -> UserLang.unlock(user, lang))
			.toList();

		if (!toUnlock.isEmpty()) {
			userLangRepository.saveAll(toUnlock);
			log.info("User {}: {}개 언어 해금됨 -> {}", user.getGithubUsername(), toUnlock.size(), newUnlockLangNames);
		} else {
			log.info("User {}: 해금할 새로운 언어 없음", user.getGithubUsername());
		}
	}
}
