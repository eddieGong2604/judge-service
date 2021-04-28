package com.csit314.testservice.entity;

import com.csit314.testservice.entity.enums.Verdict;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class TestCase extends BaseEntity {
    private Verdict verdict;
    private String input;
    private String expectedOutput;
    @ManyToOne
    @JoinColumn(name = "attempt_id")
    @JsonIgnore
    private Attempt attempt;
}
