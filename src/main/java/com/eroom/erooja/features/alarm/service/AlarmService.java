package com.eroom.erooja.features.alarm.service;

import com.eroom.erooja.domain.model.Alarm;
import com.eroom.erooja.features.alarm.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public Page<Alarm> getMessageAllByUid(String uid, Pageable pageable){
        return alarmRepository.findAllByRecevier_Uid(uid, pageable);
    }
}
