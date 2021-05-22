package com.csit314.testservice.controller.response;

import com.csit314.testservice.entity.enums.TestCaseSize;
import com.csit314.testservice.entity.enums.TestCaseType;
import com.csit314.testservice.entity.enums.Verdict;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCaseResponseDto implements Comparable<TestCaseResponseDto> {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID testCaseId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Verdict verdict;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String input;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stdout;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String expectedOutput;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TestCaseSize size;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TestCaseType type;

    @Override
    public int compareTo(TestCaseResponseDto testCaseResponseDto) {
        if(type.getPriority() == testCaseResponseDto.getType().getPriority()){
            return size.getPriority() - testCaseResponseDto.size.getPriority();
        }
        else{
            return type.getPriority() - testCaseResponseDto.type.getPriority();
        }
    }
}
