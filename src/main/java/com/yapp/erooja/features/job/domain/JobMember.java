package com.yapp.erooja.features.job.domain;

import com.yapp.erooja.features.members.domain.Members;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class JobMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_group_id")
    private JobGroup jobGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mebers_id")
    private Members member;
}
