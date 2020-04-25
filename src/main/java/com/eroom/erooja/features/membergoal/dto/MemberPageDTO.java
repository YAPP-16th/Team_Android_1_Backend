package com.eroom.erooja.features.membergoal.dto;

import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.features.member.dto.MemberDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPageDTO {
    private List<MemberDTO> members;
    private int size;
    private int totalPages;
    private long totalElement;

    public static MemberPageDTO of(Page<Members> memberPage) {
        return MemberPageDTO.builder()
                    .members(memberPage.stream().map(MemberDTO::of).collect(Collectors.toList()))
                    .size(memberPage.getSize())
                    .totalPages(memberPage.getTotalPages())
                    .totalElement(memberPage.getTotalElements())
                .build();
    }
}
