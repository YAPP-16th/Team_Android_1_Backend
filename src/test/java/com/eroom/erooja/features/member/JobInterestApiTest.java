package com.eroom.erooja.features.member;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.domain.model.MemberAuth;
import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.auth.kakao.json.KakaoUserJSON;
import com.eroom.erooja.features.auth.service.MemberAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Profile({"test"}) @ActiveProfiles({"test"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobInterestApiTest {
    private static final Logger logger = LoggerFactory.getLogger(JobInterestApiTest.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String BASE_END_POINT = "/api/v1/member/jobInterest";

    private final Long MOCKED_KAKAO_ID = 100000L;

    private final String MOCKED_UID = "KAKAO@" + MOCKED_KAKAO_ID;

    private final String MOCKED_TOKEN = "MOCKED_TOKEN";

    private final String WRONG_TOKEN = "WRONG_TOKEN";

    private final MockMvc mockMvc;

    @SpyBean
    private JwtTokenProvider jwtTokenProvider;

    private final MemberAuthService memberAuthService;

    @BeforeEach
    public void setUp() {
        doReturn(MOCKED_UID).when(jwtTokenProvider).getUidFromHeader(MOCKED_TOKEN);
    }

    @Test
    @DisplayName("잘못된 JWT 토큰이 인증 헤더에 포함되면 JWT_MALFORMED_TOKEN 에러 메세지를 반환한다")
    public void whenMalformedJwtHasInAuthHeaderThenShouldReturnErrorMessage() throws Exception {
        mockMvc.perform(
                get(BASE_END_POINT)
                        .header(HttpHeaders.AUTHORIZATION, WRONG_TOKEN)
                        .accept(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason(ErrorEnum.JWT_MALFORMED_TOKEN.getMessage()));
    }

    @Test
    @Transactional
    @DisplayName("존재하지 않는 직군 아이디를 유저에 추가하는 요청을 받으면 JOB_INTEREST_NOT_EXISTS 에러 메세지를 반환한다")
    public void whenUserRequestsToAddNonExistsJobInterestThenShouldReturnErrorMessage() throws Exception {
        MemberAuth memberAuth = memberAuthService.create(KakaoUserJSON.builder().id(MOCKED_KAKAO_ID).build());
        Members member = memberAuth.getMember();

        String jobInterestDto = "{" + "\"jobInterestId\": \"1\"" + "}";

        mockMvc.perform(
                put(BASE_END_POINT)
                        .header(HttpHeaders.AUTHORIZATION, MOCKED_TOKEN)
                        .accept(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                        .content(jobInterestDto))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(status().reason(ErrorEnum.JOB_INTEREST_NOT_EXISTS.getMessage()));
    }



}
