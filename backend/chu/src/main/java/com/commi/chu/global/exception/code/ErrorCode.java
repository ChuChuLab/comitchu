package com.commi.chu.global.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "잘못된 입력값입니다"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "C002", "요청한 리소스를 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", "서버 내부 오류가 발생했습니다"),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "C004", "로그인이 필요한 서비스입니다"),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "C005", "접근 권한이 없습니다"),

    // Auth
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "유효하지 않은 액세스 토큰입니다"),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다"),

    // Chu
    CHU_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"H001", "이미지 생성에 실패했습니다."),
    CHU_NOT_FOUND(HttpStatus.NOT_FOUND, "H002", "츄 정보를 찾을 수 없습니다"),

    // Github
    GITHUB_GRAPHQL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,"G001","GitHub GraphQL API 요청에 실패했습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
