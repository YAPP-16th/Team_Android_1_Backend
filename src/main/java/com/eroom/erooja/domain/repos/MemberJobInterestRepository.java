package com.eroom.erooja.domain.repos;

import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.model.MemberJobInterest;
import com.eroom.erooja.domain.model.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberJobInterestRepository extends JpaRepository<MemberJobInterest, Long> {
    List<MemberJobInterest> getAllByMember_UidAndJobInterest_JobInterestType(String uid, JobInterestType jobInterestType);

    List<MemberJobInterest> getAllByMember_UidAndJobInterest_JobGroup_Id(String uid, Long JobGroupId);

    Boolean existsByMember_UidAndJobInterest_Id(String uid, Long id);

    MemberJobInterest getByMember_UidAndJobInterest_Id(String uid, Long id);

    List<MemberJobInterest> getAllByMember_Uid(String uid);
}
