package com.ssafy.api.controller;

import com.ssafy.api.dto.member.request.MemberLoginRequest;
import com.ssafy.api.dto.member.request.MemberSignUpRequest;
import com.ssafy.api.dto.member.response.MemberResponse;
import com.ssafy.api.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/member")
public class MemberController {
    private final MemberService memberService;
    public static final String AUTHORIZATION_HEADER = "Authorization";


    @PostMapping(value="/signup")
    public ResponseEntity<Void> signUp(@Validated @RequestBody MemberSignUpRequest request){
        memberService.signUp(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<MemberResponse> login(@Validated @RequestBody MemberLoginRequest request){
        MemberResponse response = memberService.login(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(response.getAccessToken())
                .body(response);
    }

    // 로그아웃
    @PostMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        String token = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token =  token.substring(7);
        }

        memberService.logout(token);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }


}
