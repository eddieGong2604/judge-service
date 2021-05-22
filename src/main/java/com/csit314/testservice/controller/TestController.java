package com.csit314.testservice.controller;

import com.csit314.testservice.config.CachedTestCase;
import com.csit314.testservice.controller.request.SourceCodeRequestDto;
import com.csit314.testservice.controller.response.AttemptResponseDto;
import com.csit314.testservice.controller.response.TestCaseResponseDto;
import com.csit314.testservice.entity.enums.TestCaseType;
import com.csit314.testservice.service.FileStorageService;
import com.csit314.testservice.service.JudgeService;
import com.csit314.testservice.service.TestCaseGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class TestController {

    private final JudgeService judgeService;
    @Autowired
    public TestController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @PostMapping(value = "/attempts")
    public ResponseEntity<?> createAttempt(@RequestBody SourceCodeRequestDto dto,@RequestParam("testCaseTypes") List<String> testCaseTypes) throws InterruptedException {
        AttemptResponseDto attempt = judgeService.createAttempt(dto, TestCaseType.convertFromStringsToEnums(testCaseTypes));
        return new ResponseEntity<>(attempt, HttpStatus.OK);
    }

    @PutMapping(value = "/attempts/{attemptId}/{testCaseId}")
    public ResponseEntity<?> executeTestCase(@PathVariable("attemptId") UUID attemptId,@PathVariable("testCaseId") UUID testCaseId) throws InterruptedException {
        TestCaseResponseDto testCase = judgeService.executeTestCase(attemptId,testCaseId);
        return new ResponseEntity<>(testCase, HttpStatus.OK);
    }
    @GetMapping(value = "/attempts/{attemptId}")
    public ResponseEntity<?> getAttemptById(@PathVariable("attemptId") UUID attemptId) {
        AttemptResponseDto attemptResponseDto = judgeService.getAttemptById(attemptId);
        return new ResponseEntity<>(attemptResponseDto, HttpStatus.OK);
    }
    @GetMapping(value = "/attempts/{attemptId}/{testCaseId}")
    public ResponseEntity<?> getTestCase(@PathVariable("attemptId") UUID attemptId,@PathVariable("testCaseId") UUID testCaseId) {
        TestCaseResponseDto testCase = judgeService.getTestCase(attemptId,testCaseId);
        return new ResponseEntity<>(testCase, HttpStatus.OK);
    }
    @GetMapping(value = "/attempts/pass-percentage")
    public ResponseEntity<?> getAttemptsPassPercentage() {
        List<AttemptResponseDto> dtos = judgeService.getAttemptsPassPercentage();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

}
