package com.csit314.testservice.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CachedTestCase {
    private String input;
    private String expectedOutput;
}
