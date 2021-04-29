package com.csit314.testservice.controller;

import com.csit314.testservice.controller.request.SourceCodeRequestDto;
import com.csit314.testservice.controller.response.TestCaseResponseDto;
import com.csit314.testservice.service.JudgeService;
import com.csit314.testservice.service.TestCaseGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class TestController {

    private final JudgeService judgeService;
    private final TestCaseGenerationService testCaseGenerationService;
    @Autowired
    public TestController(JudgeService judgeService, TestCaseGenerationService testCaseGenerationService) {
        this.judgeService = judgeService;
        this.testCaseGenerationService = testCaseGenerationService;
    }

    @GetMapping(value = "/testcases")
    public ResponseEntity<?> getTestCases() throws InterruptedException {
        List<TestCaseResponseDto> testCaseResponseDtos = testCaseGenerationService.generateTestCase();
        return new ResponseEntity<>(testCaseResponseDtos, HttpStatus.OK);
    }



}
