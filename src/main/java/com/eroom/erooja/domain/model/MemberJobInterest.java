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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "job_interest_id")
    private JobInterest jobInterest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Members member;
}
