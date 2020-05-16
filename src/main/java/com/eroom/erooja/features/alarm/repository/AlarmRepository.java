package com.eroom.erooja.features.alarm.repository;

import com.eroom.erooja.domain.model.Alarm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>{
    Page<Alarm> findAllByRecevier_Uid(String uid, Pageable pageable);
    Page<Alarm> findAllByRecevier_UidAndIsCheckedIsFalse(String uid, Pageable pageable);
}

