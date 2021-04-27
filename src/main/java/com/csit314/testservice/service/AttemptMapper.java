package com.csit314.testservice.service;

import com.csit314.testservice.controller.response.AttemptResponseDto;
import com.csit314.testservice.entity.Attempt;

public interface AttemptMapper {
    AttemptResponseDto toDto(Attempt attempt);
}
