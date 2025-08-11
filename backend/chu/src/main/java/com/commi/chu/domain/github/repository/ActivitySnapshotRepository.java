package com.commi.chu.domain.github.repository;

import com.commi.chu.domain.github.entity.ActivitySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ActivitySnapshotRepository extends JpaRepository<ActivitySnapshot, Integer> {

    Optional<ActivitySnapshot> findByUser_Id(Integer userId);

    Optional<ActivitySnapshot> findFirstByUserIdAndCalculatedAtGreaterThanEqualAndCalculatedAtLessThanOrderByCalculatedAtDesc(Integer user_id,
        LocalDateTime calculatedAt, LocalDateTime calculatedAt2);

}
