package com.csit314.testservice.repository;


import java.util.UUID;

import com.csit314.testservice.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AttemptRepository extends JpaRepository<Attempt, UUID>, JpaSpecificationExecutor<Attempt> {


}
