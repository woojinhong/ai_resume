package com.resume.ai_resume.entity;


import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 이력서 섹션 테이블
// 이력서의 변경이 잦은 섹션을 나눠 해시 값으로 저장
// 수정이 있을 시 변경이 생긴 해시 섹션만 변경 가능.
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ResumeSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이력서 섹션 유형 (자기소개, 기술 스책, 경력, 프로젝트, 학력 )
    @Column(length = 50)
    private String sectionType;

    // 이력서 섹션 내용 (예) 경력 "의류 쇼핑몰 프로젝트, 아동 발달 치료 전문가 매칭 플랫폼, 팀 프로젝트 리더 경험 등")
    private String sectionContent;

    // 이력서 섹션 해시 값 unique = true (경력 hash, 기술 스택 hash, 자기소개 hash, 프로젝트 hash)
    private String sectionHash;

    // update date
    @LastModifiedDate
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime modifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id") // 외래 키 컬럼
    private Resumes resume;
}
