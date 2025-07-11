package com.commi.chu.global.exception.handler;

import com.commi.chu.global.common.response.CommonResponse;
import com.commi.chu.global.exception.CustomException;
import com.commi.chu.global.exception.code.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 비즈니스 로직에서 발생하는 커스텀 예외 처리 (필수)
     */
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<CommonResponse<Object>> handleCustomException(CustomException e, HttpServletRequest request) {
        log.error("[CustomException] {} {}: {}",
                request.getMethod(),
                request.getRequestURI(),
                e.getMessage()
        );
        return CommonResponse.error(e.getErrorCode(), e.getParameters());
    }

    /**
     * 2. 처리되지 않은 모든 예외를 잡는 최후의 안전망 (필수)
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResponse<Object>> handleException(Exception e, HttpServletRequest request) {
        log.error("[UnhandledException] {} {}: {}",
                request.getMethod(),
                request.getRequestURI(),
                e.getMessage(),
                e); // 개발 중에는 스택 트레이스를 모두 확인하기 위해 e를 함께 로깅하는 것이 좋습니다.
        return CommonResponse.error(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}