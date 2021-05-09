package com.csit314.testservice.service;

import com.csit314.testservice.controller.response.AttemptResponseDto;
import com.csit314.testservice.entity.Attempt;

import java.util.List;

public interface AttemptMapper {
    AttemptResponseDto toDto(Attempt attempt);
    List<AttemptResponseDto> toDtos(List<Attempt> attempt);
    AttemptResponseDto toDtoWithPercentage(Attempt attempt);
    List<AttemptResponseDto> toDtosWithPercentage(List<Attempt> attempt);
}
