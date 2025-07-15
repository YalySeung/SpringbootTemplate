package com.sprinboottemplate.springboottempate.conroller;

import com.sprinboottemplate.springboottempate.dto.SampleDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ResponseEntity<SampleDto> getSampleById(
            @Parameter(description = "샘플 ID", example = "1") @PathVariable Long id) {
        SampleDto sample = database.get(id);
        return sample != null ? ResponseEntity.ok(sample) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "이름으로 조회", description = "RequestParam을 사용해 샘플 조회")
    @GetMapping
    public ResponseEntity<SampleDto> getSampleByName(
            @Parameter(description = "이름", example = "홍길동") @RequestParam String name) {
        return database.values().stream()
                .filter(s -> s.getName().equalsIgnoreCase(name))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "샘플 생성", description = "샘플 데이터를 생성")
    @PostMapping
    public ResponseEntity<SampleDto> createSample(
            @RequestBody SampleDto request) {
        request.setId(idSequence++);
        database.put(request.getId(), request);
        return ResponseEntity.ok(request);
    }

    @Operation(summary = "샘플 수정", description = "샘플 데이터를 수정")
    @PutMapping("/{id}")
    public ResponseEntity<SampleDto> updateSample(
            @Parameter(description = "샘플 ID", example = "1") @PathVariable Long id,
            @RequestBody SampleDto request) {
        if (!database.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        request.setId(id);
        database.put(id, request);
        return ResponseEntity.ok(request);
    }

    @Operation(summary = "샘플 삭제", description = "샘플 데이터를 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSample(
            @Parameter(description = "샘플 ID", example = "1") @PathVariable Long id) {
        if (!database.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }
        database.remove(id);
        return ResponseEntity.noContent().build();
    }
}