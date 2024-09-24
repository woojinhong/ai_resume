package com.resume.ai_resume.service;

import com.resume.ai_resume.dao.InterviewersRepository;
import com.resume.ai_resume.dto.LoginRequestDto;
import com.resume.ai_resume.dto.SignupRequestDto;
import com.resume.ai_resume.entity.Interviewers;
import com.resume.ai_resume.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class InterviewersService {

    private final InterviewersRepository interviewersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public void signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String name = requestDto.getName();



        Optional<Interviewers> checkEmail = interviewersRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 등록
        Interviewers user = new Interviewers(name, email, password);
        interviewersRepository.save(user);
    }


    public void login(LoginRequestDto requestDto, HttpServletResponse res) {
        System.out.println("requestDto.getEmail() = " + requestDto.getEmail());
        System.out.println("requestDto.getPassword() = " + requestDto.getPassword());
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();

        // 사용자 확인
        Interviewers interviewers = interviewersRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, interviewers.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // JWT 생성 및 쿠키에 저장 후 Response 객체에 추가
        String token = jwtUtil.createToken(interviewers.getEmail());
        jwtUtil.addJwtToCookie(token, res);
    }
}


