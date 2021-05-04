package com.csit314.testservice.service;

import com.csit314.testservice.config.CachedTestCase;
import com.csit314.testservice.controller.response.AttemptResponseDto;
import com.csit314.testservice.controller.response.TestCaseResponseDto;
import com.csit314.testservice.entity.Attempt;
import com.csit314.testservice.entity.TestCase;
import com.csit314.testservice.integration.judge0.dto.response.SubmissionBatchResponseDto;

import java.util.List;

public interface TestCaseMapper {
    TestCaseResponseDto toDto(TestCase testCase);

    List<TestCaseResponseDto> toDtos(List<TestCase> testCaseList);

    List<TestCaseResponseDto> fromCachedToResponseTestCase(List<CachedTestCase> cachedTestCases);

    List<CachedTestCase> fromSubmissionBatchResponseToCachedTestCase(SubmissionBatchResponseDto submissionBatchResponseDto);

    List<TestCaseResponseDto> fromSubmissionBatchResponseToTestcaseResponseDto(SubmissionBatchResponseDto submissionBatchResponseDto);


}
