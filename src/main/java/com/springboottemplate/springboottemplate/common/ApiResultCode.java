package com.springboottemplate.springboottemplate.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiResultCode {

    // ✅ 성공
    SUCCESS("1000", "요청이 성공적으로 처리되었습니다.", HttpStatus.OK),

    // ❌ 클라이언트 오류
    INVALID_REQUEST("2000", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER("2001", "필수 요청값이 누락되었습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER("2002", "요청값이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    MALFORMED_JSON("2003", "요청 본문이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED("2004", "지원하지 않는 HTTP 메서드입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("2005", "인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("2006", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    NOT_FOUND("2007", "요청한 자원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // ❌ 서버 오류
    INTERNAL_ERROR("9000", "서버 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}