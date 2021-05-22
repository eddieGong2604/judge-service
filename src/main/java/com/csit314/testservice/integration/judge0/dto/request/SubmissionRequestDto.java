package com.csit314.testservice.integration.judge0.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubmissionRequestDto {
    private String source_code;
    private Integer language_id;
    private String stdin;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String expected_output;
}
