package com.eroom.erooja.features.members.domain;

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

    @Column(nullable = false, unique = true)
    private String uid;

    private String passwd;
    private String thirdPartyProvider;
    private String thirdPartyUserInfo;

    @Column(nullable = false)
    private Boolean isThirdParty;

    @OneToOne(cascade=CascadeType.ALL)
    private Members member;
}
