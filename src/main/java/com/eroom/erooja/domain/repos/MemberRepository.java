package com.eroom.erooja.domain.repos;

import com.eroom.erooja.domain.model.Members;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Members, String> {
    Boolean existsByNickname(String nickname);

    @Query("SELECT m.imagePath FROM Members m " +
            "JOIN MemberGoal g " +
            "ON m.uid = g.uid " +
            "AND g.goalId = :goalId")
    List<String> getUserImageListByGoalId(Long goalId, Pageable pageable);
}
