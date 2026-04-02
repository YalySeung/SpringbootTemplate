package com.springboottemplate.springboottemplate.common.dto;


import com.springboottemplate.springboottemplate.common.ApiResultCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Schema(name = "BaseResponse", description = "공통 API 응답 포맷")
public class BaseResponse<T> {

    @Schema(description = "응답 코드", example = "200")
    private String code;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .code(ApiResultCode.SUCCESS.getCode())
                .message(ApiResultCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> from(ApiResultCode resultCode, T data) {
        return BaseResponse.<T>builder()
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(data)
                .build();
    }

    public static BaseResponse<Void> error(ApiResultCode resultCode) {
        return BaseResponse.<Void>builder()
                .code(resultCode.getCode())
                .message(resultCode.getMessage())
                .data(null)
                .build();
    }

    public static BaseResponse<Void> error(ApiResultCode resultCode, String message) {
        return BaseResponse.<Void>builder()
                .code(resultCode.getCode())
                .message(message)
                .data(null)
                .build();
    }

    public static BaseResponse<Void> error(String code, String message) {
        return BaseResponse.<Void>builder()
                .code(code)
                .message(message)
                .data(null)
                .build();
    }
}