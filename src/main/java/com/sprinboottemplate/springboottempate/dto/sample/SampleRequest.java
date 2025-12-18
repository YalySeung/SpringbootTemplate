package com.sprinboottemplate.springboottempate.dto.sample;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Sample 데이터 요청")
public class SampleRequest {
    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "설명", example = "테스트 샘플입니다")
    private String description;
}
