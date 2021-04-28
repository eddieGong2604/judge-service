package com.csit314.testservice.controller.response;

import com.csit314.testservice.entity.enums.Verdict;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResponseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Verdict verdict;
    private String input;
    private String expectedOutput;
}
