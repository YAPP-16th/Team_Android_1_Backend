package com.yapp.erooja.features.todo.domain;

import com.yapp.erooja.features.goaljoin.domain.GoalJoin;
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
    @NonNull
    private Boolean isEnd;
    @NonNull
    private int priority;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_join_id")
    GoalJoin goalJoin;
}
