package com.csit314.testservice.integration.judge0.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionBatchResponseDto {
    private List<SubmissionResponseDto> submissions = new ArrayList<>();
}
