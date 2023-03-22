//package com.ssafy.controller;
//
//import com.ssafy.dto.member.request.MemberSignUpRequest;
//import com.ssafy.service.MemberService;
//import lombok.AllArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@RequiredArgsConstructor
//public class MemberController {
//    private final MemberService memberService;
//
//    @PostMapping(value="/signup")
//    public ResponseEntity<Void> signUp(@Validated @RequestBody MemberSignUpRequest request){
//        memberService.signUp(request);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .build();
//    }
//}
