package com.eroom.erooja.domain.model;

import lombok.*;

import javax.persistence.*;

@Builder
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class MemberJobInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_group_id")
    private JobInterest jobInterest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Members member;
}
