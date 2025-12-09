package com.sprinboottemplate.springboottempate.common;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

    private final ApiResultCode resultCode;

    public ApiException(ApiResultCode resultCode, String customMessage) {
        super(customMessage);
        this.resultCode = resultCode;
    }
}