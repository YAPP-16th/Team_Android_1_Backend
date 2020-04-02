package com.yapp.erooja.features.members.domain;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@Entity
public class MemberAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NonNull
    @Column(unique = true)
    private String uid;
    private String passwd;
    private String third_party_provider;
    private String third_party_user_info;
    @NonNull
    private Boolean is_third_party;
    @OneToOne(cascade=CascadeType.ALL)
    private Members member;
}
