package com.csit314.testservice.service.impl;

import com.csit314.testservice.controller.response.TestCaseResponseDto;
import com.csit314.testservice.entity.TestCase;
import com.csit314.testservice.service.TestCaseMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class TestCaseMapperImpl implements TestCaseMapper {

    @Override
    public TestCaseResponseDto toDto(TestCase testCase) {
        TestCaseResponseDto testCaseResponseDto = new TestCaseResponseDto();
        testCaseResponseDto.setInput(testCase.getInput());
        testCaseResponseDto.setVerdict(testCase.getVerdict());
        testCaseResponseDto.setExpectedOutput(testCase.getExpectedOutput());
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
}
