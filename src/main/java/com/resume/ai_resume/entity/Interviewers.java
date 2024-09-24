package com.resume.ai_resume.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;


// 면접관(인사 담당자)의 정보를 저장합니다
// 면접관의 정보를 토대로 지원자 이력서의 정보를 로드합니다.
@Entity
@Getter
@NoArgsConstructor
public class Interviewers extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 면접관 이름
    private String name;
    // 면접관 이메일
    private String email;
    // 면접관 비밀번호
    private String password;

    public Interviewers(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}



