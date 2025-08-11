package com.commi.chu.domain.github.repository;

import com.commi.chu.domain.github.entity.ActivitySnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface ActivitySnapshotRepository extends JpaRepository<ActivitySnapshot, Integer> {

    Optional<ActivitySnapshot> findByUser_Id(Integer userId);

    Optional<ActivitySnapshot> findFirstByUserIdAndCalculatedAtBetweenOrderByCalculatedAtDesc(Integer userId, Instant start, Instant end);

}
