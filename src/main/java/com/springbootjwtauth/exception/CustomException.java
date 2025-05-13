package com.springbootjwtauth.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 예외 메시지는 ErrorCode에 정의된 message로 설정
        this.errorCode = errorCode;
    }

}
