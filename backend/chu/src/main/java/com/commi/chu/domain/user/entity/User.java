package com.commi.chu.domain.user.entity;

import com.commi.chu.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Column(name = "github_id", unique = true, nullable = false)
    private Long githubId;

    @Column(name = "gitgub_username", nullable = false, length = 50)
    private String githubUsername;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void updateProfile(String githubUsername, String avatarUrl) {
        this.githubUsername = githubUsername;
        this.avatarUrl = avatarUrl;
    }

}