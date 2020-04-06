package com.eroom.erooja.domain.model;

import com.eroom.erooja.domain.common.AuditProperties;
import com.eroom.erooja.features.member.dto.MemberDTO;
import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = {"uid", "nickname", "imagePath"}, callSuper = false)
public class Members extends AuditProperties {
    @Id
    private String uid;

    private String nickname;

    private String imagePath;

    @OneToOne(fetch = FetchType.LAZY)
    private MemberAuth memberAuth;

    @Builder
    public Members(String uid, String nickname, String imagePath, MemberAuth memberAuth) {
        this.uid = uid;
        this.nickname = nickname;
        this.imagePath = imagePath;
        this.memberAuth = memberAuth;
    }

    public static Members of(String uid, MemberDTO memberDTO) {
        return Members.builder()
                    .uid(uid)
                    .nickname(memberDTO.getNickname())
                    .imagePath(memberDTO.getImagePath())
                .build();
    }

    public static Members of(MemberDTO memberDTO) {
        return Members.builder()
                .uid(memberDTO.getUid())
                .nickname(memberDTO.getNickname())
                .imagePath(memberDTO.getImagePath())
                .build();
    }
}
