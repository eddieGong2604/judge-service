package com.csit314.testservice.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttemptResponseDto {
    private UUID attemptId;
    private String code;
    private List<TestCaseResponseDto> testCases;
}
