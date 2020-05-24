package com.eroom.erooja.features.alarm.dto;

import com.eroom.erooja.domain.enums.AlarmType;
import com.eroom.erooja.domain.model.Alarm;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.model.Members;
import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageDTO {
    private Long id;
    private String title;
    private String content;
    private Boolean isChecked;
    private AlarmType messageType;
    private Long goalId;

    public static MessageDTO of(Alarm alarm){
        return MessageDTO.builder()
                .id(alarm.getId())
                .content(alarm.getContent())
                .isChecked(alarm.getIsChecked())
                .messageType(alarm.getMessageType())
                .title(alarm.getTitle())
                .goalId(alarm.getGoal().getId()).build();
    }
}
