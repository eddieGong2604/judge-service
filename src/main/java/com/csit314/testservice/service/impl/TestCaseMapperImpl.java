package com.csit314.testservice.service.impl;

import com.csit314.testservice.config.CachedTestCase;
import com.csit314.testservice.controller.response.TestCaseResponseDto;
import com.csit314.testservice.entity.TestCase;
import com.csit314.testservice.integration.judge0.dto.response.SubmissionBatchResponseDto;
import com.csit314.testservice.integration.judge0.dto.response.SubmissionResponseDto;
import com.csit314.testservice.service.TestCaseMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TestCaseMapperImpl implements TestCaseMapper {

    @Override
    public TestCaseResponseDto toDto(TestCase testCase) {
        TestCaseResponseDto testCaseResponseDto = new TestCaseResponseDto();
        testCaseResponseDto.setTestCaseId(testCase.getId());
        testCaseResponseDto.setVerdict(testCase.getVerdict());
        testCaseResponseDto.setSize(testCase.getSize());
        testCaseResponseDto.setType(testCase.getType());
        return testCaseResponseDto;
    }

    @Override
    public TestCaseResponseDto toDtoWithInputAndOutput(TestCase testCase) {
        TestCaseResponseDto testCaseResponseDto = new TestCaseResponseDto();
        testCaseResponseDto.setTestCaseId(testCase.getId());
        testCaseResponseDto.setInput(testCase.getInput());
        testCaseResponseDto.setVerdict(testCase.getVerdict());
        testCaseResponseDto.setExpectedOutput(testCase.getExpectedOutput());
        testCaseResponseDto.setStdout(testCase.getStdout());
        testCaseResponseDto.setSize(testCase.getSize());
        testCaseResponseDto.setType(testCase.getType());
        return testCaseResponseDto;
    }

    @Override
    public List<TestCaseResponseDto> toDtos(List<TestCase> testCaseList) {
        ArrayList<TestCaseResponseDto> dtos = new ArrayList<>();
        for(TestCase testCase : testCaseList){
            dtos.add(toDto(testCase));
        }
        return dtos;
    }

    @Override
    public List<TestCaseResponseDto> fromCachedToResponseTestCase(List<CachedTestCase> cachedTestCases) {
        ArrayList<TestCaseResponseDto> dtos = new ArrayList<>();
        for(CachedTestCase testCase : cachedTestCases){
            TestCaseResponseDto testCaseResponseDto = new TestCaseResponseDto();
            testCaseResponseDto.setInput(testCase.getInput());
            testCaseResponseDto.setExpectedOutput(testCase.getExpectedOutput());
            testCaseResponseDto.setSize(testCase.getSize());
            testCaseResponseDto.setType(testCase.getType());

            dtos.add(testCaseResponseDto);
        }
        return dtos;
    }


    @Override
    public List<CachedTestCase> fromSubmissionBatchResponseToCachedTestCase(SubmissionBatchResponseDto submissionBatchResponseDto) {
        ArrayList<CachedTestCase> dtos = new ArrayList<>();
        for(SubmissionResponseDto submissionResponseDto : submissionBatchResponseDto.getSubmissions()){
            CachedTestCase testCaseResponseDto = new CachedTestCase();
            testCaseResponseDto.setInput(submissionResponseDto.getStdin());
            testCaseResponseDto.setExpectedOutput(submissionResponseDto.getStdout());
            dtos.add(testCaseResponseDto);
        }
        return dtos;
    }
    @Override
    public List<TestCaseResponseDto> fromSubmissionBatchResponseToTestcaseResponseDto(SubmissionBatchResponseDto submissionBatchResponseDto) {
        ArrayList<TestCaseResponseDto> dtos = new ArrayList<>();
        for(SubmissionResponseDto submissionResponseDto : submissionBatchResponseDto.getSubmissions()){
            TestCaseResponseDto testCaseResponseDto = new TestCaseResponseDto();
            testCaseResponseDto.setInput(submissionResponseDto.getStdin());
            testCaseResponseDto.setExpectedOutput(submissionResponseDto.getStdout());
            dtos.add(testCaseResponseDto);
        }
        return dtos;
    }
}
