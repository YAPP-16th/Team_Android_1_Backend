package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
@Builder
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

    private LocalDateTime startDt;
    private LocalDateTime endDt;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GoalJobInterest> goalJobInterests;

    @Builder
    public Goal(Long id, String title, String description, int joinCount, Boolean isEnd, Boolean isDateFixed,
                LocalDateTime startDt, LocalDateTime endDt, LocalDateTime createDt, LocalDateTime updateDt) {
        super(createDt, updateDt);
    }
}
