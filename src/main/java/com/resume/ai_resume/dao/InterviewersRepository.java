package com.resume.ai_resume.dao;

import com.resume.ai_resume.entity.Interviewers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterviewersRepository extends JpaRepository <Interviewers, Long> {

    Optional<Interviewers> findByEmail(String email);
}
