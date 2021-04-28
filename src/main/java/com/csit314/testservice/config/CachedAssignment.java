package com.csit314.testservice.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CachedAssignment {
    private String assignmentName;
    private String code;
    private List<CachedTestCase> cachedTestCases = new ArrayList<>();
}
