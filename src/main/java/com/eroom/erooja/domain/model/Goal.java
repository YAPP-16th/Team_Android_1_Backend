package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.tomcat.jni.Local;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@EqualsAndHashCode(of = {"id"}, callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Goal extends AuditProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private int joinCount = 1;

    @Column(nullable = false)
    private Boolean isEnd = false;

    @Column(nullable = false)
    private Boolean isDateFixed;

    private LocalDateTime startDt = LocalDateTime.now();

    private LocalDateTime endDt;

    @JsonIgnore
    @OneToMany(mappedBy = "goal", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoalJobInterest> goalJobInterests;

    @JsonProperty(value = "jobInterests")
    public List<JobInterest> jobInterests() {
        if (goalJobInterests == null) {
            return Collections.emptyList();
        }
        return goalJobInterests.stream()
                .map(GoalJobInterest::getJobInterest)
                .collect(Collectors.toList());
    }

    @Builder
    public Goal(LocalDateTime createDt, LocalDateTime updateDt, Long id,
                String title, String description, int joinCount, Boolean isEnd, Boolean isDateFixed,
                LocalDateTime startDt, LocalDateTime endDt, List<GoalJobInterest> goalJobInterests) {
        super(createDt, updateDt);
        this.id = id;
        this.title = title;
        this.description = description;
        this.joinCount = joinCount;
        this.isEnd = isEnd;
        this.isDateFixed = isDateFixed;
        this.startDt = startDt;
        this.endDt = endDt;
        this.goalJobInterests = goalJobInterests;
    }

    public int increaseJoinCount(int count) {
        return this.joinCount += count;
    }

    public Boolean isExpire() {
        return this.endDt.isAfter(LocalDateTime.now());
    }
}
