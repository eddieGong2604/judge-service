package com.csit314.testservice.service;

import com.csit314.testservice.controller.request.SourceCodeRequestDto;
import com.csit314.testservice.controller.response.AttemptResponseDto;
import com.csit314.testservice.controller.response.TestCaseResponseDto;

import java.util.List;
import java.util.UUID;

public interface JudgeService {
    AttemptResponseDto createAttempt(SourceCodeRequestDto sourceCodeRequestDto) throws InterruptedException;
    TestCaseResponseDto executeTestCase(UUID attemptId, UUID testCaseId) throws InterruptedException;
    AttemptResponseDto getAttemptById(UUID attemptId);
    TestCaseResponseDto getTestCase(UUID attemptId, UUID testCaseId);
    List<AttemptResponseDto> getAttemptsPassPercentage();

}
