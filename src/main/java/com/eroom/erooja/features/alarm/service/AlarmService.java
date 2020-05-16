package com.eroom.erooja.features.alarm.service;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
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

    public Page<Alarm> getMessageUncheckedByUid(String uid, Pageable pageable){
        return alarmRepository.findAllByRecevier_UidAndIsCheckedIsFalse(uid, pageable);
    }

    public Alarm changeStateToChecked(String uid, Long alarmId){
        Alarm message = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new EroojaException(ErrorEnum.ALARM_MESSAGE_NOT_FOUND));

        message.setIsChecked(true);

        if(!(message.checkMessageIsOwn(uid)))
            throw new EroojaException(ErrorEnum.ALARM_MESSAGE_NOT_ALLOWED);

        return alarmRepository.save(message);
    }
}
