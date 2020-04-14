package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.field.GoalJobInterest_;
import lombok.*;

import javax.persistence.*;


@Builder
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class GoalJobInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = GoalJobInterest_.jobInterest)
    private JobInterest jobInterest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = GoalJobInterest_.goal)
    private Goal goal;
}
