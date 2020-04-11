package com.eroom.erooja.domain.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Builder
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor @AllArgsConstructor
@Getter
@Entity
public class JobInterest {
    public static final int ROOT_LEVEL = 0;
    public static final int MAX_LEVEL = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Min(value = ROOT_LEVEL) @Max(value = MAX_LEVEL)
    private int level;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_group_id")
    private JobInterest jobGroup;
}