package com.eroom.erooja.domain.repos;

import com.eroom.erooja.domain.model.MemberJobInterest;
import com.eroom.erooja.domain.model.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberJobInterestRepository extends JpaRepository<MemberJobInterest, Long> {
    List<MemberJobInterest> getAllByMember(Members build);
    Boolean existsByMember_UidAndJobInterest_Id(String uid, Long id);
    MemberJobInterest getByMember_UidAndJobInterest_Id(String uid, Long id);
}
