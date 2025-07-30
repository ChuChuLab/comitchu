package com.commi.chu.domain.github.entity;

import java.time.LocalDate;

import com.commi.chu.domain.user.entity.User;
import com.commi.chu.global.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "activity_snapshot_log")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivitySnapshotLog extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "commit_count", nullable = false)
	private Integer commitCount;

	@Column(name = "pr_count", nullable = false)
	private Integer prCount;

	@Column(name = "issue_count", nullable = false)
	private Integer issueCount;

	@Column(name = "review_count", nullable = false)
	private Integer reviewCount;

	@Column(name = "activity_date", nullable = false)
	private LocalDate activityDate;

}
