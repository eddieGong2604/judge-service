package com.csit314.testservice.entity;

import com.csit314.testservice.entity.enums.Verdict;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestCase extends BaseEntity {
    private Verdict verdict;
    @Basic(fetch = FetchType.LAZY)
    private String input;
    private String expectedOutput;
    private String stdout;
    @ManyToOne
    @JoinColumn(name = "attempt_id")
    @JsonIgnore
    private Attempt attempt;
}
