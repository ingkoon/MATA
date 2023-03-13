package com.ssafy.api.common.exception.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    public ErrorResponse(final String message) {
        this.message = message;
    }

    public ErrorResponse of(){
        return new ErrorResponse(message);
    }
}
