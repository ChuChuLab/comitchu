package com.commi.chu.domain.github.entity;

import com.commi.chu.domain.user.entity.User;
import com.commi.chu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_snapshot")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivitySnapshot extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "commit_count", nullable = false)
    private Integer commitCount;

    @Column(name = "pr_count", nullable = false)
    private Integer prCount;

    @Column(name = "issue_count", nullable = false)
    private Integer issueCount;

    @Column(name = "review_count", nullable = false)
    private Integer reviewCount;

    @LastModifiedDate
    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;

    //업데이트 메소드
    public ActivitySnapshot updateSnapshot(Integer commitCount, Integer prCount, Integer issueCount, Integer reviewCount) {
        this.commitCount = commitCount;
        this.prCount = prCount;
        this.issueCount = issueCount;
        this.reviewCount = reviewCount;
        this.calculatedAt = LocalDateTime.now();
        return this;
    }

}
