package com.eroom.erooja.domain.repos;

import com.eroom.erooja.domain.model.MemberJobInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberJobInterestRepository extends JpaRepository<MemberJobInterest, Long> {
}
