package com.csit314.testservice.config;

import com.csit314.testservice.entity.enums.TestCaseSize;
import com.csit314.testservice.entity.enums.TestCaseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CachedTestCase {
    private String input;
    private String expectedOutput;
    private TestCaseType type;
    private TestCaseSize size;
}
