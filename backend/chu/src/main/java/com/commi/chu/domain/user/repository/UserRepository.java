package com.commi.chu.domain.user.repository;

import com.commi.chu.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByGithubId(Long githubId);

    // GitHub 계정이 연결된 사용자만 조회
    List<User> findByGithubUsernameIsNotNull();
}
