package com.csit314.testservice.entity.enums;

import java.util.ArrayList;
import java.util.List;

public enum TestCaseType {
    edgeCase(1), shortestPathOnly(3), bothShortestAndSecondShortestPath(2);

    private final int priority;
    TestCaseType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
    public static List<TestCaseType> convertFromStringsToEnums(List<String> enumStrings){
        ArrayList<TestCaseType> results = new ArrayList<>();
        for(String enumString : enumStrings){
            results.add(TestCaseType.valueOf(enumString));
        }
        return results;
    }
}
