package com.eroom.erooja.features.member.dto;

import com.eroom.erooja.domain.model.Members;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@NoArgsConstructor
@Getter
@ToString
public class MemberDTO {
    @Setter
    private String uid;
    private String nickname;
    private String imagePath;

    @Builder
    public MemberDTO(String uid, String nickname, String imagePath) {
        this.uid = uid;
        this.nickname = nickname;
        this.imagePath = imagePath;
    }

    public static MemberDTO of(Members members) {
        return MemberDTO.builder()
                    .uid(members.getUid())
                    .nickname(members.getNickname())
                    .imagePath(members.getImagePath())
                .build();
    }
}
