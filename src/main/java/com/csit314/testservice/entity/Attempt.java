package com.csit314.testservice.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;

@Data
@Entity
public class Attempt extends BaseEntity {
    private String code;
    @Transient
    private List<TestCase> testCases;
}
