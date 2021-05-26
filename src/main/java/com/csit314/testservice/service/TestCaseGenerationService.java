package com.csit314.testservice.service;

import com.csit314.testservice.config.CachedTestCase;
import com.csit314.testservice.controller.response.TestCaseResponseDto;
import com.csit314.testservice.entity.enums.TestCaseType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface TestCaseGenerationService {
    void init() throws InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException;
    List<CachedTestCase> getTestCaseByTypes(List<TestCaseType> testCaseTypes) throws InterruptedException;
}
