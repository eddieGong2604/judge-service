package com.csit314.testservice.entity;

import com.csit314.testservice.entity.enums.TestCaseSize;
import com.csit314.testservice.entity.enums.TestCaseType;
import com.csit314.testservice.entity.enums.Verdict;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Verdict verdict = Verdict.Running;
    @Basic(fetch = FetchType.LAZY)
    private String input;
    private String expectedOutput;
    private String stdout;
    @ManyToOne
    @JoinColumn(name = "attempt_id")
    @JsonIgnore
    private Attempt attempt;
    private TestCaseType type;
    private TestCaseSize size;
}
