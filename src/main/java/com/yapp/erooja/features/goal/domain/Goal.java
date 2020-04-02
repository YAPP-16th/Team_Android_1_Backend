package com.yapp.erooja.features.goal.domain;

import com.yapp.erooja.features.job.domain.JobGoal;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Lob
    private String description;
    @NonNull
    private int joinCount=0;
    @NonNull
    private Boolean isEnd=false;
    @NonNull
    private Boolean isDateFixed;
    private LocalDateTime startDt;
    private LocalDateTime endDt;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;
    @UpdateTimestamp
    private LocalDateTime changeDt;
    @OneToMany(mappedBy="goal", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<JobGoal> jobGoals;
}
