package com.sprinboottemplate.springboottempate.dto.sample;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface SampleMapper {
    @Mapping(source = "id", target = "id")
    SampleDto toDto(SampleRequest request);

    @Mapping(source = "id", target = "id")
    SampleRequest toRequest(SampleDto sampleDto);
}
