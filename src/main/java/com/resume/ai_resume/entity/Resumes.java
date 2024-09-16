package com.resume.ai_resume.entity;

import jakarta.persistence.*;


// 지원자 이력서 테이블
@Entity
public class Resumes extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //지원자 이름
    private String name;
    //지원자 이메일
    private String email;
    //값 타입 객체 || 내장 객체
    @Embedded
    private Address address;
    // 핸드폰 번호
    private String phoneNo;

    // 이력서 파일 해시 값 노중복
    @Column(nullable = false, unique = true)
    private String resumeHash;

    //지원자 이력서 학력
    private String education;
    // JSON 데이터를 큰 문자열로 저장
    // 지원자 chatgpt 분석 결과
    @Column(columnDefinition = "TEXT")
    private String analysisResult;


}
