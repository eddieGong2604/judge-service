package com.csit314.testservice.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttemptResponseDto {
    private String code;
    private List<TestCaseResponseDto> testCases;
}
