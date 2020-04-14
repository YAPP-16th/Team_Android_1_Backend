package com.eroom.erooja.domain.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @Column(nullable = false)
    private Boolean isEnd;

    @Column(nullable = false)
    private int priority;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="uid"),
            @JoinColumn(name="goal_id")
    })
    MemberGoal memberGoal;
}
