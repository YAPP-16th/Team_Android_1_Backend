package com.eroom.erooja.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class MemberGoalPK implements Serializable {
    @EqualsAndHashCode.Include
    @Id
    private String uid;
    @EqualsAndHashCode.Include
    @Column(name="goal_id")
    @Id
    private Long goalId;
}
