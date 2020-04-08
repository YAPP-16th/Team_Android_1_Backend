package com.eroom.erooja.domain.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Builder
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor @AllArgsConstructor
@Getter
@Entity
public class JobInterest {
    public static final int ROOT_LEVEL = 0;
    public static final int MAX_LEVEL = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Min(value = ROOT_LEVEL) @Max(value = MAX_LEVEL)
    private int level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "super_job_id")
    private JobInterest superJobInterest;
}
