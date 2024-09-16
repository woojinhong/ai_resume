package com.resume.ai_resume.entity;

import jakarta.persistence.*;


// 지원자 기술스택 리스트 저장용 엔티티
@Entity
public class Skills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 지원자 기술스택
    @Column(length = 10)
    private String skill;
}
