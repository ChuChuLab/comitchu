package com.commi.chu.domain.chu.repository;

import java.util.Optional;

import com.commi.chu.domain.chu.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Integer> {
	Optional<Language> findByLang(String langName);
}
