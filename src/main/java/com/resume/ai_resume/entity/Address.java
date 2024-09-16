package com.resume.ai_resume.entity;

import jakarta.persistence.Embeddable;

// 주소 테이블
// 지원자의 city, street, zipcode 를 나눠 저장할 내장 객체
// 별도의 테이블이 아닌 Resumes 테이블의 값
@Embeddable
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
