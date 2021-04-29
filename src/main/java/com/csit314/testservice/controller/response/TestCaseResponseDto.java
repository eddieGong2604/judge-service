package com.csit314.testservice.controller.response;

import com.csit314.testservice.entity.enums.Verdict;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResponseDto {
    private UUID testCaseId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Verdict verdict;
    private String input;
    private String expectedOutput;
}
