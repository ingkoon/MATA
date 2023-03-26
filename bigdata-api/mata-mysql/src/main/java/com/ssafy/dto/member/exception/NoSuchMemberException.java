package com.ssafy.dto.member.exception;

public class NoSuchMemberException extends RuntimeException{
    public NoSuchMemberException() {
        this("회원 정보를 찾을 수 없습니다.");
    }

    public NoSuchMemberException(String message) {
        super(message);
    }
}
