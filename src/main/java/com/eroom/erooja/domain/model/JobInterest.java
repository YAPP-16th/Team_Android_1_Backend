package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.enums.JobInterestType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Builder
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor @AllArgsConstructor
@Getter
@Entity
@ToString(of = {"id", "name", "jobInterestType"})
public class JobInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private JobInterestType jobInterestType;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_group_id")
    private JobInterest jobGroup;
}