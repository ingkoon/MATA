package com.ssafy.api.dto.project.exception;

public class NoSuchProjectException extends RuntimeException{
    public NoSuchProjectException() {
        this("프로젝트 정보를 찾을 수 없습니다.");
    }

    public NoSuchProjectException(String message) {
        super(message);
    }
}
