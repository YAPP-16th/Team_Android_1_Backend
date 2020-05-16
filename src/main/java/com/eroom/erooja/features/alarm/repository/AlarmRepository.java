package com.eroom.erooja.features.alarm.repository;

import com.eroom.erooja.domain.model.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long>{
}

