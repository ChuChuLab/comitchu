package com.commi.chu.domain.chu.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.LastModifiedDate;

import com.commi.chu.domain.user.entity.User;
import com.commi.chu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "chu")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Chu extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "exp", nullable = false)
    private Integer exp;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ChuStatus status; // e.g., "NORMAL", "HUNGRY", "HAPPY"

    @Column(name = "lang", nullable = false, length = 50)
    private String lang;

    @Column(name = "background", nullable = false, length = 50)
    private String background;

    @Column(name = "last_status_updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime lastStatusUpdatedAt;

    @Column(name = "last_leveled_date_kst")
    private LocalDate lastLeveledDateKst;

    //동시성 충돌 방지를 위한 version
    @Version
    private Long version = 0L;

    public void updateStatus(ChuStatus newStatus) {
        this.status = newStatus;
    }


    public void updateLang(String lang) {
        this.lang = lang;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateBackground(String background) {
        this.background = background;
    }

    public void levelUp(int level, int exp) {
        this.level = level;
        this.exp = exp;
    }

    public void markLeveledToday(LocalDate kstDate) {
        this.lastLeveledDateKst = kstDate;
    }
}
