package com.resume.ai_resume.controller;

import com.resume.ai_resume.dto.LoginRequestDto;
import com.resume.ai_resume.dto.SignupRequestDto;
import com.resume.ai_resume.entity.UserRoleEnum;
import com.resume.ai_resume.jwt.JwtUtil;
import com.resume.ai_resume.service.InterviewersService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class InterviewersController {

    private final InterviewersService interviewersService;
    @GetMapping("/user/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/user/signup")
    public String signup(SignupRequestDto requestDto) {
        interviewersService.signup(requestDto);
        return "redirect:/api/user/login";
    }

    @PostMapping("/user/login")
    public String login(LoginRequestDto requestDto, HttpServletResponse res) {
        System.out.println("requestDto.getEmail() = " + requestDto.getEmail());
        System.out.println("requestDto.getPassword() = " + requestDto.getPassword());
        interviewersService.login(requestDto,res);
        return "redirect:/index";
    }

}
