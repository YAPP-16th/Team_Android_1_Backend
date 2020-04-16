package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(of = {"id"})
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

    @Builder
    public Goal(Long id, String title, String description, int joinCount,
                Boolean isEnd, Boolean isDateFixed, LocalDateTime startDt,
                LocalDateTime endDt, LocalDateTime createDt, LocalDateTime updateDt) {
        super(createDt, updateDt);
        this.id = id;
        this.title=title;
        this.description=description;
        this.joinCount=joinCount;
        this.isEnd=isEnd;
        this.isDateFixed=isDateFixed;
        this.startDt=startDt;
        this.endDt=endDt;
    }

}
