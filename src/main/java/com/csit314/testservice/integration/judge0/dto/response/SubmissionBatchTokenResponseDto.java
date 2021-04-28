package com.csit314.testservice.integration.judge0.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionBatchTokenResponseDto {
    private List<SubmissionTokenResponseDto> submissions;
}
