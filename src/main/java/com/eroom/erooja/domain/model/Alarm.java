package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import com.eroom.erooja.domain.enums.AlarmType;
import com.eroom.erooja.features.alarm.dto.InsertMessageDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(of = {"id"}, callSuper = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Alarm extends AuditProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String content;

    @Column(nullable = false)
    private Boolean isChecked=false;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AlarmType messageType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", updatable = false, insertable = false)
    private Members receiver;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goalId", updatable = false, insertable = false)
    private Goal goal;

    public Boolean checkMessageIsOwn(String uid){
        return this.receiver.getUid().equals(uid);
    }

    public static Alarm of(InsertMessageDTO insertMessage){
        return Alarm.builder()
                .content(insertMessage.getContent())
                .title(insertMessage.getTitle())
                .messageType(insertMessage.getMessageType())
                .receiver(Members.builder().uid(insertMessage.getReceiverUid()).build())
                .goal(Goal.builder().id(insertMessage.getGoalId()).build()).build();
    }
}
