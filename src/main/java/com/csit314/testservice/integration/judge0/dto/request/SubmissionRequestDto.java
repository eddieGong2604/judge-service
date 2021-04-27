package com.csit314.testservice.integration.judge0.dto.request;

import lombok.Data;

@Data
public class SubmissionRequestDto {
    private String source_code;
    private Integer language_id;
    private String stdin;

}
