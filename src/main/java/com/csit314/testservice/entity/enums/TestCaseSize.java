package com.csit314.testservice.entity.enums;

public enum TestCaseSize {
    Large(1), Small(3), Medium(2);
    private final int priority;
    TestCaseSize(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
