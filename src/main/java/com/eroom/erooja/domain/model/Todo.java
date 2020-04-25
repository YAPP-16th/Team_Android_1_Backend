package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class Todo extends AuditProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;

    @Column(nullable = false)
    private Boolean isEnd = false;

    @Column(nullable = false)
    private int priority;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name="uid"),
            @JoinColumn(name="goal_id")
    })
    MemberGoal memberGoal;
}
