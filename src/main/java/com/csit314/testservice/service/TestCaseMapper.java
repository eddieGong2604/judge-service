package com.csit314.testservice.service;

import com.csit314.testservice.controller.response.AttemptResponseDto;
import com.csit314.testservice.controller.response.TestCaseResponseDto;
import com.csit314.testservice.entity.Attempt;
import com.csit314.testservice.entity.TestCase;

import java.util.List;

public interface TestCaseMapper {
    TestCaseResponseDto toDto(TestCase attempt);
    List<TestCaseResponseDto> toDtos(List<TestCase> testCaseList);
}
