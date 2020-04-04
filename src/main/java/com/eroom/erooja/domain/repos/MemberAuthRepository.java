package com.eroom.erooja.domain.repos;

import com.eroom.erooja.domain.model.MemberAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAuthRepository extends JpaRepository<MemberAuth, String> {
}
