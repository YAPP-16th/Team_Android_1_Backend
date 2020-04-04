package com.eroom.erooja.domain.model;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class JobGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private int level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "super_job_id")
    private JobGroup superJobGroup;
}
