package com.springboottemplate.springboottemplate.dto.sample;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Sample 데이터 요청")
public class SampleRequest {
    @Schema(description = "ID", example = "1")
    private Long id;

    @NotBlank(message = "이름은 필수입니다.")
    @Size(max = 50, message = "이름은 50자 이하여야 합니다.")
    @Schema(description = "이름", example = "홍길동")
    private String name;

    @NotBlank(message = "설명은 필수입니다.")
    @Size(max = 200, message = "설명은 200자 이하여야 합니다.")
    @Schema(description = "설명", example = "테스트 샘플입니다")
    private String description;
}
