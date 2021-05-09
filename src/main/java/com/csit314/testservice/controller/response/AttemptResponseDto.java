package com.csit314.testservice.controller.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttemptResponseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID attemptId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double passPercentage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TestCaseResponseDto> testCases;
}
