package com.eroom.erooja.features.alarm.dto;

import com.eroom.erooja.domain.enums.AlarmType;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InsertMessageDTO {
    private String title;
    private String content;
    private AlarmType messageType;
    private String receiverUid;
    private Long goalId;
}
