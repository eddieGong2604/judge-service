package com.csit314.testservice.controller.response;

import lombok.Data;

import java.util.List;

@Data
public class AttemptResponseDto {
    private String code;
    private List<TestCaseResponseDto> testCases;
}
