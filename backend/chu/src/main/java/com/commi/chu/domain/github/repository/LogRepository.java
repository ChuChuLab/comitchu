package com.commi.chu.domain.github.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.commi.chu.domain.github.entity.ActivitySnapshotLog;

public interface LogRepository extends JpaRepository<ActivitySnapshotLog, Integer> {

	//최신 3일간의 log 데이터를 가져오는 메서드
	List<ActivitySnapshotLog> findTop3ByUserIdOrderByActivityDateDesc(Integer userId);
}
