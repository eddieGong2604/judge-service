package com.csit314.testservice.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.csit314.testservice.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, UUID>, JpaSpecificationExecutor<TestCase> {

    Optional<List<TestCase>> findByAttemptId(UUID attemptId);
}
