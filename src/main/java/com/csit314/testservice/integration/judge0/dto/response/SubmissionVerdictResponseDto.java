package com.csit314.testservice.integration.judge0.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionVerdictResponseDto {
    private String sdtout;
    private StatusResponseDto status;
}