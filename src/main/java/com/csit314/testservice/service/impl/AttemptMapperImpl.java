package com.csit314.testservice.service.impl;

import com.csit314.testservice.controller.response.AttemptResponseDto;
import com.csit314.testservice.entity.Attempt;
import com.csit314.testservice.service.AttemptMapper;
import com.csit314.testservice.service.TestCaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttemptMapperImpl implements AttemptMapper{

    private final TestCaseMapper testCaseMapper;

    @Override
    public AttemptResponseDto toDto(Attempt attempt) {
        AttemptResponseDto attemptResponseDto = new AttemptResponseDto();
        attemptResponseDto.setCode(attempt.getCode());
        attemptResponseDto.setTestCases(testCaseMapper.toDtos(attempt.getTestCases()));
        return attemptResponseDto;
    }
}
