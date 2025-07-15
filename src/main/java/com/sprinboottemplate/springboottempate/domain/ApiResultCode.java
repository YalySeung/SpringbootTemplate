package com.sprinboottemplate.springboottempate.domain;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiResultCode {

    // ✅ 성공
    SUCCESS(HttpStatus.OK, "요청이 성공적으로 처리되었습니다."),
    CREATED(HttpStatus.CREATED, "리소스가 성공적으로 생성되었습니다."),

    // ✅ 클라이언트 오류
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // ✅ 서버 오류
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생했습니다.");

    private final int code;
    private final String message;

    ApiResultCode(HttpStatus status, String message) {
        this.code = status.value();
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(code);
    }
}