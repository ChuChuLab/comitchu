package com.commi.chu.domain.chu.entity;

import java.time.LocalDateTime;

import com.commi.chu.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_lang")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLang {

    @Id
    @Column(name ="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lang_id", nullable = false)
    private Language lang;

    @Column(name = "unlocked_at", nullable = false)
    private LocalDateTime unlockedAt;

    //언어 해금 편의 메서드
    public static UserLang unlock(User user, Language lang) {
        return UserLang.builder()
            .user(user)
            .lang(lang)
            .unlockedAt(LocalDateTime.now())
            .build();
    }
}
