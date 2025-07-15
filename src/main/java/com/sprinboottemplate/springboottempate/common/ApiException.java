package com.sprinboottemplate.springboottempate.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final ApiResultCode resultCode;

    public ApiException(ApiResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public ApiException(ApiResultCode resultCode, String customMessage) {
        super(customMessage);
        this.resultCode = resultCode;
    }

    public HttpStatus getHttpStatus() {
        return resultCode.getHttpStatus();
    }
}