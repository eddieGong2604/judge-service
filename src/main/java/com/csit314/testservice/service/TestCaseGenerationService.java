package com.csit314.testservice.service;

import com.csit314.testservice.config.CachedTestCase;
import com.csit314.testservice.controller.response.TestCaseResponseDto;

import java.util.List;

public interface TestCaseGenerationService {
    List<CachedTestCase> generateTestCase() throws InterruptedException;
}
