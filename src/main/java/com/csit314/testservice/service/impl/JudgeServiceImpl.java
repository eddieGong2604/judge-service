package com.csit314.testservice.service.impl;

import com.csit314.testservice.controller.request.SourceCodeRequestDto;
import com.csit314.testservice.controller.response.AttemptResponseDto;
import com.csit314.testservice.controller.response.TestCaseResponseDto;
import com.csit314.testservice.entity.Attempt;
import com.csit314.testservice.entity.TestCase;
import com.csit314.testservice.entity.enums.Verdict;
import com.csit314.testservice.integration.judge0.Judge0ServiceIntegration;
import com.csit314.testservice.integration.judge0.dto.response.SubmissionVerdictResponseDto;
import com.csit314.testservice.repository.AttemptRepository;
import com.csit314.testservice.repository.TestCaseRepository;
import com.csit314.testservice.service.AttemptMapper;
import com.csit314.testservice.service.JudgeService;
import com.csit314.testservice.service.TestCaseGenerationService;
import com.csit314.testservice.service.TestCaseMapper;
import com.csit314.testservice.service.constants.SourceCodeConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JudgeServiceImpl implements JudgeService {
    private final TestCaseRepository testCaseRepository;
    private final AttemptRepository attemptRepository;
    private final TestCaseGenerationService testCaseGenerationService;
    private final AttemptMapper attemptMapper;
    private final Judge0ServiceIntegration judge0ServiceIntegration;
    private final TestCaseMapper testCaseMapper;
    @Override
    public AttemptResponseDto createAttempt(SourceCodeRequestDto sourceCodeRequestDto) throws InterruptedException {
        List<TestCaseResponseDto> testCaseResponseDtos = testCaseGenerationService.generateTestCase();
        /*Save new attempt to database*/
        Attempt attempt = attemptRepository.save(Attempt.builder().code(sourceCodeRequestDto.getCode()).build());
        List<TestCase> attemptTestCase = new ArrayList<>();
        /*Save attempt test case to database */
        for(TestCaseResponseDto testCaseResponseDto : testCaseResponseDtos){
            TestCase testCase = new TestCase();
            testCase.setAttempt(attempt);
            testCase.setInput(testCaseResponseDto.getInput());
            testCase.setExpectedOutput(testCaseResponseDto.getExpectedOutput());
            attemptTestCase.add(testCaseRepository.save(testCase));
        }
        attempt.setTestCases(attemptTestCase);
        return attemptMapper.toDto(attempt);
    }
    @Override
    public TestCaseResponseDto executeTestCase(UUID attemptId, UUID testCaseId) throws InterruptedException {
        /*Get test case from database*/
        Attempt attempt = attemptRepository.findById(attemptId).orElseThrow(()-> new RuntimeException("Not found"));
        TestCase testCase = testCaseRepository.findByAttemptIdAndId(attemptId, testCaseId).orElseThrow(() -> new RuntimeException("not found"));
        SubmissionVerdictResponseDto submissionVerdictResponseDto = judge0ServiceIntegration.executeTestCase(attempt.getCode(), testCase.getInput(),
                testCase.getExpectedOutput());
        if(submissionVerdictResponseDto.getStatus().getId() == 3){
            testCase.setVerdict(Verdict.ACCEPTED);
        }
        else{
            testCase.setVerdict(Verdict.FAILED);
        }
        testCase.setStdout(submissionVerdictResponseDto.getStdout());
        return testCaseMapper.toDto(testCaseRepository.save(testCase));
    }

    @Override
    public AttemptResponseDto getAttemptById(UUID attemptId) {
        Attempt attempt = attemptRepository.findById(attemptId).orElseThrow(()-> new RuntimeException("Not found"));
        List<TestCase> testCaseList = testCaseRepository.findByAttemptId(attemptId).orElse(new ArrayList<>());
        attempt.setTestCases(testCaseList);
        return attemptMapper.toDto(attempt);
    }

    @Override
    public TestCaseResponseDto getTestCase(UUID attemptId, UUID testCaseId) {
        TestCase testCase = testCaseRepository.findByAttemptIdAndId(attemptId, testCaseId).orElseThrow(() -> new RuntimeException("not found"));
        return testCaseMapper.toDto(testCase);
    }

    @Override
    public List<AttemptResponseDto> getAttemptsPassPercentage() {
       List<Attempt> attempts = attemptRepository.findAll(Sort.by(Sort.Direction.ASC, "createdOn"));
       return attemptMapper.toDtosWithPercentage(attempts);
    }
}
