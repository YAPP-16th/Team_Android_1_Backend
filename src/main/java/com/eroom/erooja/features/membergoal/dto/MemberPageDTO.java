package com.eroom.erooja.features.membergoal.dto;

import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.features.member.dto.MemberDTO;
import com.eroom.erooja.features.member.dto.MemberWithJobInterestsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberPageDTO {
    private List<MemberWithJobInterestsDTO> members;
    private int size;
    private int totalPages;
    private long totalElement;

    public static MemberPageDTO of(Page<Members> memberPage, Map<String, List<JobInterest>> jobInterestByUid) {
        return MemberPageDTO.builder()
                    .members(
                            memberPage.stream()
                                .map(el ->
                                        MemberWithJobInterestsDTO.of(el, jobInterestByUid.get(el.getUid())))
                                .collect(Collectors.toList()))
                    .size(memberPage.getSize())
                    .totalPages(memberPage.getTotalPages())
                    .totalElement(memberPage.getTotalElements())
                .build();
    }
}
