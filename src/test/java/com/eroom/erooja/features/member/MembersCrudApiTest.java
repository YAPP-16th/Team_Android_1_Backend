package com.eroom.erooja.features.member;

import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.member.dto.MemberDTO;
import com.eroom.erooja.features.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles({"test"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MembersCrudApiTest {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void updateEachField() throws Exception {
        String mockId = "KAKAO@testId";

        MemberDTO memberDTO = MemberDTO.builder()
                .uid(mockId)
                .nickname("수정된 닉네임")
                .build();

        given(jwtTokenProvider.getUsernameFromToken("[TOKEN]"))
                .willReturn(mockId);

        given(memberService.updateNotNullPropsOf(Members.of(memberDTO)))
                .willReturn(Members.of(memberDTO));

        mockMvc.perform(
                post("/api/v1/member/" + mockId)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer [TOKEN]")
                    .accept(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                    .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                    .content(objectMapper.writeValueAsBytes(memberDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("uid", is(mockId)))
                .andExpect(jsonPath("nickname", is("수정된 닉네임")));
    }
}
