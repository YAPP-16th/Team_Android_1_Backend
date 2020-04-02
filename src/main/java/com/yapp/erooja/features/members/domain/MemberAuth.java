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

    @Column(updatable = false, unique = true)
    private String uid;

    private String passwd;
    private String thirdPartyProvider;
    private String thirdpartyUserInfo;

    @Column(updatable = false)
    private Boolean isThirdParty;
    
    @OneToOne(cascade=CascadeType.ALL)
    private Members member;
}
