package com.resume.ai_resume.entity;

import jakarta.persistence.*;


// 지원자 테이블
// 지원자의 기본 정보 저장 (gpt open api based)
// 자동 저장 가능
@Entity
public class Applicants extends TimeStamped {
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


}
