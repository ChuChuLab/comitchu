package com.commi.chu.domain.user.repository;

import com.commi.chu.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByGithubId(Long githubId);

    // 탈퇴하지 않은 사용자들 목록 조회
    @Query("""
            select u 
                        from User u 
                                    where u.githubUsername is not null
                                                and u.deletedAt is null
            """)
    List<User> findActiveGithubUsers();
}
