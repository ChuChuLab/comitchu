package com.commi.chu.domain.chu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commi.chu.domain.chu.entity.UserLang;

public interface UserLangRepository extends JpaRepository<UserLang, Integer> {

	List<UserLang> findByUserId(Integer userId);
}
