package com.eroom.erooja.domain.repos;

import com.eroom.erooja.domain.model.JobInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobInterestRepository extends JpaRepository<JobInterest, Long> {
}
