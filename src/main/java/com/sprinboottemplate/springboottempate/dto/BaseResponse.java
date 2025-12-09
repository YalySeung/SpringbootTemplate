package com.sprinboottemplate.springboottempate.dto;


import com.sprinboottemplate.springboottempate.common.ApiResultCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "BaseResponse", description = "공통 API 응답 포맷")
public class BaseResponse<T> {

    @Schema(description = "응답 코드", example = "200")
    private String code;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private String message;

    @Schema(description = "응답 데이터")
    private T data;

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ApiResultCode.SUCCESS.getCode(), "요청이 성공적으로 처리되었습니다.", data);
    }

    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(ApiResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> BaseResponse<T> error(String code, String message) {
        return new BaseResponse<>(code, message, null);
    }

    // ✅ ApiResultCode를 사용한 응답 (data 포함)
    public static <T> BaseResponse<T> from(ApiResultCode resultCode, T data) {
        return new BaseResponse<>(resultCode.getCode(), resultCode.getMessage(), data);
    }

    // ✅ ApiResultCode를 사용한 응답 (data 없이)
    public static <T> BaseResponse<T> from(ApiResultCode resultCode) {
        return from(resultCode, null);
    }
}