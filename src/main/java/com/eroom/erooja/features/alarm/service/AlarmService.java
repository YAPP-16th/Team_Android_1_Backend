package com.eroom.erooja.features.alarm.service;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.model.Alarm;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.features.alarm.dto.InsertMessageDTO;
import com.eroom.erooja.features.alarm.dto.MessageDTO;
import com.eroom.erooja.features.alarm.repository.AlarmRepository;
import com.eroom.erooja.features.membergoal.dto.GoalJoinMemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public Page<MessageDTO> getMessageAllByUid(String uid, Pageable pageable){
        Page<Alarm> alarmsPage = alarmRepository.findAllByReceiver_Uid(uid, pageable);
        return convertAlarmPage2DTO(alarmsPage);
    }

    public Page<MessageDTO> getMessageUncheckedByUid(String uid, Pageable pageable){
        Page<Alarm> alarmsPage = alarmRepository.findAllByReceiver_UidAndIsCheckedIsFalse(uid, pageable);
        return convertAlarmPage2DTO(alarmsPage);
    }

    public Alarm changeStateToChecked(String uid, Long alarmId){
        Alarm message = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new EroojaException(ErrorEnum.ALARM_MESSAGE_NOT_FOUND));

        message.setIsChecked(true);

        if(!(message.checkMessageIsOwn(uid)))
            throw new EroojaException(ErrorEnum.ALARM_MESSAGE_NOT_ALLOWED);

        return alarmRepository.save(message);
    }

    public Alarm insertMessage(InsertMessageDTO insertMessage){
        Alarm message = Alarm.of(insertMessage);
        return alarmRepository.save(message);
    }

    private Page<MessageDTO> convertAlarmPage2DTO(Page<Alarm> origin) {
        return new PageImpl<MessageDTO>(
                origin.stream()
                        .map(mg -> MessageDTO.of(mg))
                        .collect(Collectors.toList()),
                origin.getPageable(), origin.getTotalElements());
    }
}
