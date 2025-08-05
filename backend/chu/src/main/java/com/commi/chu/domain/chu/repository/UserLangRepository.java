package com.commi.chu.domain.chu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.commi.chu.domain.chu.entity.UserLang;

public interface UserLangRepository extends JpaRepository<UserLang, Integer> {

	List<UserLang> findByUserId(Integer userId);

	Optional<UserLang> findByUserIdAndLangId(Integer userId, Integer langId);
}
