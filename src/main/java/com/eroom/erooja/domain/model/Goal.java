package com.eroom.erooja.domain.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Lob
    private String description;

    @Column(nullable = false)
    private int joinCount = 0;

    @Column(nullable = false)
    private Boolean isEnd = false;

    @Column(nullable = false)
    private Boolean isDateFixed;

    private LocalDateTime startDt;
    private LocalDateTime endDt;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;

    @UpdateTimestamp
    private LocalDateTime changeDt;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoalJobInterest> goalJobInterests;
}
