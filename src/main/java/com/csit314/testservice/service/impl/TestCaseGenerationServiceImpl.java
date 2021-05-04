package com.csit314.testservice.service.impl;

import com.csit314.testservice.config.CachedTestCase;
import com.csit314.testservice.controller.response.TestCaseResponseDto;
import com.csit314.testservice.integration.judge0.Judge0ServiceIntegration;
import com.csit314.testservice.integration.judge0.dto.request.SubmissionBatchRequestDto;
import com.csit314.testservice.integration.judge0.dto.request.SubmissionRequestDto;
import com.csit314.testservice.integration.judge0.dto.response.SubmissionBatchResponseDto;
import com.csit314.testservice.service.TestCaseGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TestCaseGenerationServiceImpl implements TestCaseGenerationService {
    private final TestCaseMapperImpl testCaseMapper;
    private final Judge0ServiceIntegration judge0ServiceIntegration;

    @Override
    public List<TestCaseResponseDto> generateTestCase() throws InterruptedException {
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
        return testCaseMapper.fromSubmissionBatchResponseToTestcaseResponseDto(submissionBatchResponseDto);
    }
    private int inputGenerator() {
        Random random = new Random();
        return random.nextInt(10);
    }
}
