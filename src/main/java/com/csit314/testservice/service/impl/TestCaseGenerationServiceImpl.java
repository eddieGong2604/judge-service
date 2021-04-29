package com.csit314.testservice.service.impl;

import com.csit314.testservice.config.CachedAssignment;
import com.csit314.testservice.config.CachedTestCase;
import com.csit314.testservice.controller.response.TestCaseResponseDto;
import com.csit314.testservice.integration.judge0.Judge0ServiceIntegration;
import com.csit314.testservice.integration.judge0.dto.request.SubmissionBatchRequestDto;
import com.csit314.testservice.integration.judge0.dto.request.SubmissionRequestDto;
import com.csit314.testservice.integration.judge0.dto.response.SubmissionBatchResponseDto;
import com.csit314.testservice.repository.AttemptRepository;
import com.csit314.testservice.repository.TestCaseRepository;
import com.csit314.testservice.service.TestCaseGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TestCaseGenerationServiceImpl implements TestCaseGenerationService {
    private final TestCaseMapperImpl testCaseMapper;
    private final RedisTemplate<String, CachedAssignment> assignmentCache;
    private final Judge0ServiceIntegration judge0ServiceIntegration;
    private final String ASSIGNMENT_CACHE_KEY = "CSCI203_ASSIGNMENT3";

    @Override
    public List<TestCaseResponseDto> generateTestCase() throws InterruptedException {
        final ValueOperations<String, CachedAssignment> operations = assignmentCache.opsForValue();

        if (!assignmentCache.hasKey(ASSIGNMENT_CACHE_KEY)) {
            operations.set(ASSIGNMENT_CACHE_KEY, CachedAssignment.builder().assignmentName(ASSIGNMENT_CACHE_KEY).code("#include <iostream> \n using namespace std; \n int main(){int a;cin >> a;cout<< a;return 0;}").build());
        }

        if (assignmentCache.hasKey(ASSIGNMENT_CACHE_KEY) && (operations.get(ASSIGNMENT_CACHE_KEY)).getCachedTestCases() != null) {
            return testCaseMapper.fromCachedToResponseTestCase(Objects.requireNonNull(operations.get(ASSIGNMENT_CACHE_KEY)).getCachedTestCases());
        }

        SubmissionBatchRequestDto submissionBatchRequestDto = new SubmissionBatchRequestDto();
        String sourceCode = Objects.requireNonNull(operations.get(ASSIGNMENT_CACHE_KEY)).getCode();
        /*Generate 5 test cases*/
        for (int i = 0; i < 5; i++) {
            submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(sourceCode).stdin(String.valueOf(inputGenerator())).language_id(52).build());
        }

        /*Get expected output for each test case*/
        SubmissionBatchResponseDto submissionBatchResponseDto = judge0ServiceIntegration.getExpectedOutputBatch(submissionBatchRequestDto);
        List<CachedTestCase> cachedTestCases = testCaseMapper.fromSubmissionBatchResponseToCachedTestCase(submissionBatchResponseDto);

        /*Save to cache*/
        CachedAssignment updatedCachedAssignment = CachedAssignment.builder().cachedTestCases(cachedTestCases).assignmentName(ASSIGNMENT_CACHE_KEY).code(sourceCode).build();
        assignmentCache.delete(ASSIGNMENT_CACHE_KEY);
        operations.set(ASSIGNMENT_CACHE_KEY,updatedCachedAssignment);

        return testCaseMapper.fromCachedToResponseTestCase(cachedTestCases);
    }

    private int inputGenerator() {
        Random random = new Random();
        return random.nextInt(10);
    }
}
