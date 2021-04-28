package com.csit314.testservice.integration.judge0.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionBatchRequestDto {
    private List<SubmissionRequestDto> submissions  = new ArrayList<>();
}
