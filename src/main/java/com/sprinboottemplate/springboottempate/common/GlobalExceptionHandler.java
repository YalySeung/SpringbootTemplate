package com.sprinboottemplate.springboottempate.common;

import com.sprinboottemplate.springboottempate.dto.BaseResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ 일반 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleException(Exception e) {
        e.printStackTrace(); // 디버깅용, 운영 시 제거 또는 로깅
        return ResponseEntity
                .ok()
                .body(BaseResponse.from(ApiResultCode.INTERNAL_ERROR));
    }

    // ✅ 404 또는 찾을 수 없음
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<BaseResponse<Void>> handleNotFound(NoSuchElementException e) {
        return ResponseEntity
                .ok()
                .body(BaseResponse.from(ApiResultCode.RESOURCE_NOT_FOUND));
    }

    // ✅ @Valid 실패 시
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResponseEntity
                .ok()
                .body(BaseResponse.from(ApiResultCode.MISSING_PARAMETER));
    }

    // ✅ @RequestParam 검증 실패 (예: @Min 등)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponse<Void>> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().iterator().next().getMessage();
        return ResponseEntity
                .ok()
                .body(BaseResponse.from(ApiResultCode.INVALID_PARAMETER_FORMAT));
    }

    // ✅ 우리가 정의한 API 예외 처리
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<BaseResponse<Void>> handleApiException(ApiException e) {
        ApiResultCode resultCode = e.getResultCode();
        String message = e.getMessage(); // 커스텀 메시지 가능
        return ResponseEntity
                .ok()
                .body(BaseResponse.error(resultCode.getCode(), message));
    }
}
