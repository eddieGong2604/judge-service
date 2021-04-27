package com.csit314.testservice.controller.response;

import com.csit314.testservice.entity.enums.Verdict;
import lombok.Data;


@Data
public class TestCaseResponseDto {
    private Verdict verdict;
    private String input;
    private String expectedOutput;
}
