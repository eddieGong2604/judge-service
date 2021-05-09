package com.csit314.testservice.service.impl;

import com.csit314.testservice.controller.response.AttemptResponseDto;
import com.csit314.testservice.entity.Attempt;
import com.csit314.testservice.service.AttemptMapper;
import com.csit314.testservice.service.TestCaseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttemptMapperImpl implements AttemptMapper{

    private final TestCaseMapper testCaseMapper;

    @Override
    public AttemptResponseDto toDto(Attempt attempt) {
        AttemptResponseDto attemptResponseDto = new AttemptResponseDto();
        attemptResponseDto.setAttemptId(attempt.getId());
        attemptResponseDto.setCode(attempt.getCode());
        attemptResponseDto.setTestCases(testCaseMapper.toDtos(attempt.getTestCases()));
        return attemptResponseDto;
    }
    @Override
    public AttemptResponseDto toDtoWithPercentage(Attempt attempt) {
        AttemptResponseDto attemptResponseDto = new AttemptResponseDto();
        attemptResponseDto.setAttemptId(attempt.getId());
        attemptResponseDto.setPassPercentage(attempt.getPassPercentage());
        return attemptResponseDto;
    }

    @Override
    public List<AttemptResponseDto> toDtosWithPercentage(List<Attempt> attempt) {
        List<AttemptResponseDto> attemptResponseDtos = new ArrayList<>();
        for(Attempt att : attempt){
            AttemptResponseDto attemptResponseDto = new AttemptResponseDto();
            attemptResponseDto.setAttemptId(att.getId());
            attemptResponseDto.setPassPercentage(att.getPassPercentage());
            attemptResponseDtos.add(attemptResponseDto);
        }
        return attemptResponseDtos;
    }

    @Override
    public List<AttemptResponseDto> toDtos(List<Attempt> attempt) {
        ArrayList<AttemptResponseDto> dtos = new ArrayList<>();
        for(Attempt att : attempt){
            dtos.add(toDto(att));
        }
        return dtos;
    }
}
