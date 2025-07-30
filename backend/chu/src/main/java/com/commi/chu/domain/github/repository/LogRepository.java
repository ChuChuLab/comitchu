package com.commi.chu.domain.github.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commi.chu.domain.github.entity.ActivitySnapshotLog;

public interface LogRepository extends JpaRepository<ActivitySnapshotLog, Integer> {
}
