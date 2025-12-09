package com.sprinboottemplate.springboottempate.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResultCode {

    // ✅ 성공
    SUCCESS("1000", "요청이 성공적으로 처리되었습니다."),

    // ❌ 클라이언트 오류
    INVALID_REQUEST("2000", "잘못된 요청입니다."),
    MISSING_PARAMETER("2001", "필수 파라미터가 누락되었습니다."),
    INVALID_PARAMETER_FORMAT("2002", "파라미터 형식이 유효하지 않습니다."),
    MALFORMED_JSON("2003", "요청 본문이 올바르지 않습니다."),
    METHOD_NOT_ALLOWED("2004", "지원하지 않는 HTTP 메서드입니다."),
    UNAUTHORIZED("2005", "인증 정보가 누락되었습니다."),
    INVALID_TOKEN("2006", "유효하지 않은 인증 토큰입니다."),
    FORBIDDEN("2007", "접근 권한이 없습니다."),
    NOT_FOUND("2008", "요청 경로가 잘못되었습니다."),
    DUPLICATE_REQUEST("2009", "중복된 요청입니다."),
    INVALID_FILE_TYPE("2010", "허용되지 않은 파일 형식입니다."),

    RESOURCE_NOT_FOUND("3001", "해당 항목이 존재하지 않습니다."),

    // ❌ 서버 오류
    INTERNAL_ERROR("9000", "서버 오류가 발생했습니다."),
    ;

    private final String code;
    private final String message;

    ApiResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}