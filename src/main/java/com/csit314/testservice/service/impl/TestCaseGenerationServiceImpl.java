package com.csit314.testservice.service.impl;

import com.csit314.testservice.config.CachedAssignment;
import com.csit314.testservice.controller.response.TestCaseResponseDto;
import com.csit314.testservice.integration.judge0.Judge0ServiceIntegration;
import com.csit314.testservice.integration.judge0.dto.request.SubmissionBatchRequestDto;
import com.csit314.testservice.integration.judge0.dto.request.SubmissionRequestDto;
import com.csit314.testservice.integration.judge0.dto.response.SubmissionBatchResponseDto;
import com.csit314.testservice.service.TestCaseGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TestCaseGenerationServiceImpl implements TestCaseGenerationService {
    private final TestCaseMapperImpl testCaseMapper;
    private final Judge0ServiceIntegration judge0ServiceIntegration;
    private final String ASSIGNMENT_CACHE_KEY = "CSCI203_ASSIGNMENT3";
    private final RedisTemplate<String, CachedAssignment> assignmentCache;

    @PostConstruct
    public void init() {
        if (assignmentCache.hasKey(ASSIGNMENT_CACHE_KEY)) {
            assignmentCache.delete(ASSIGNMENT_CACHE_KEY);
        }
    }

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
        /*Generate 10 test cases*/
        for (int i = 0; i < 10; i++) {
            submissionBatchRequestDto.getSubmissions().add(SubmissionRequestDto.builder().source_code(
                    /*TODO: This is the CORRECT source code that we used to generate test cases. Currently it is just a
                       correct source code for a simple problem. The task to do is
                       - Store the correct source code for assignment 3 to Google Cloud Storage
                       - Get the source code from Google Cloud Storage and replace the below string with that source code*/
                    "#include <iostream> \n using namespace std; \n int main(){int a;cin >> a;cout<< a;return 0;}"
            )
                    .stdin(
                            /*TODO: generate input for Assignment 3 in the inputGenerator() function, currently it just return random value*/
                            String.valueOf(inputGenerator())

                    ).language_id(52).build());
        }
        SubmissionBatchResponseDto submissionBatchResponseDto = judge0ServiceIntegration.getExpectedOutputBatch(submissionBatchRequestDto);
        CachedAssignment updatedCachedAssignment = CachedAssignment.builder().cachedTestCases(testCaseMapper.fromSubmissionBatchResponseToCachedTestCase(submissionBatchResponseDto)).assignmentName(ASSIGNMENT_CACHE_KEY).code("#include <iostream> \n using namespace std; \n int main(){int a;cin >> a;cout<< a;return 0;}").build();

        assignmentCache.delete(ASSIGNMENT_CACHE_KEY);
        operations.set(ASSIGNMENT_CACHE_KEY, updatedCachedAssignment);
        return testCaseMapper.fromSubmissionBatchResponseToTestcaseResponseDto(submissionBatchResponseDto);
    }

    private int inputGenerator() {
        Random random = new Random();
        return random.nextInt(10);
    }
}
