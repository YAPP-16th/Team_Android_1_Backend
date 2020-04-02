package com.yapp.erooja.features.members.domain;

import com.yapp.erooja.features.goaljoin.domain.GoalJoin;
import com.yapp.erooja.features.job.domain.JobMember;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class Members {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    private String job;
    private String image_path;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> role;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createDt;

    @UpdateTimestamp
    private LocalDateTime changeDt;

    private LocalDateTime accessDt;

    @OneToMany(mappedBy="member", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<JobMember> jobMembers;
}
