package com.commi.chu.domain.chu.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "language")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lang",nullable = false, length = 50)
    private String lang;

}
