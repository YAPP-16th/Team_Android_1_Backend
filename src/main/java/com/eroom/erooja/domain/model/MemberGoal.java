package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.eums.GoalRole;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class MemberGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GoalRole role;

    @Column(nullable = false)
    private Boolean isEnd = false;

    @Column(nullable = false)
    private int copyCount = 0;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;

    private LocalDateTime startDt;
    private LocalDateTime endDt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private Members member;

    @OneToMany(mappedBy = "memberGoal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> todoList;
}
