package com.sprinboottemplate.springboottempate.controller;

import com.sprinboottemplate.springboottempate.dto.BaseResponse;
import com.sprinboottemplate.springboottempate.dto.SampleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name = "Sample API", description = "샘플 CRUD API입니다")
@RestController
@RequestMapping("/samples")
public class SampleController {

    private final Map<Long, SampleDto> database = new HashMap<>();
    private Long idSequence = 1L;

    @Operation(summary = "ID로 조회", description = "PathVariable을 사용해 샘플 조회")
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<SampleDto>> getSampleById(
            @Parameter(description = "샘플 ID", example = "1") @PathVariable Long id) {
        SampleDto sample = database.get(id);
        return sample != null
                ? ResponseEntity.ok(BaseResponse.success(sample))
                : ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), "해당 ID의 데이터가 없습니다."));
    }

    @Operation(summary = "이름으로 조회", description = "RequestParam을 사용해 샘플 조회")
    @GetMapping
    public ResponseEntity<BaseResponse<SampleDto>> getSampleByName(
            @Parameter(description = "이름", example = "홍길동") @RequestParam String name) {
        return database.values().stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(BaseResponse::success)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), "해당 이름의 데이터가 없습니다.")));
    }

    @Operation(summary = "샘플 생성", description = "샘플 데이터를 생성")
    @PostMapping
    public ResponseEntity<BaseResponse<SampleDto>> createSample(
            @RequestBody SampleDto request) {
        request.setId(idSequence++);
        database.put(request.getId(), request);
        return ResponseEntity.ok(BaseResponse.success("샘플 생성 성공", request));
    }

    @Operation(summary = "샘플 수정", description = "샘플 데이터를 수정")
    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<SampleDto>> updateSample(
            @Parameter(description = "샘플 ID", example = "1") @PathVariable Long id,
            @RequestBody SampleDto request) {
        if (!database.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), "해당 ID의 데이터가 없습니다."));
        }
        request.setId(id);
        database.put(id, request);
        return ResponseEntity.ok(BaseResponse.success("샘플 수정 성공", request));
    }

    @Operation(summary = "샘플 삭제", description = "샘플 데이터를 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteSample(
            @Parameter(description = "샘플 ID", example = "1") @PathVariable Long id) {
        if (!database.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(BaseResponse.error(HttpStatus.NOT_FOUND.value(), "해당 ID의 데이터가 없습니다."));
        }
        database.remove(id);
        return ResponseEntity.ok(BaseResponse.success("샘플 삭제 성공", null));
    }
}
