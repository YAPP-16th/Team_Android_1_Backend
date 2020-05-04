package com.eroom.erooja.documentation.v1;

import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.model.MemberJobInterest;
import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.auth.service.MemberAuthService;
import com.eroom.erooja.features.goal.controller.GoalController;
import com.eroom.erooja.features.interest.dto.JobInterestDTO;
import com.eroom.erooja.features.interest.dto.JobInterestIdDTO;
import com.eroom.erooja.features.member.controller.MemberJobInterestController;
import com.eroom.erooja.features.member.controller.MembersController;
import com.eroom.erooja.features.member.dto.MemberDTO;
import com.eroom.erooja.features.member.service.MemberJobInterestService;
import com.eroom.erooja.features.member.service.MemberService;
import com.eroom.erooja.features.membergoal.controller.MemberGoalContoller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriHost = "erooja.eroom.com")
@ActiveProfiles({"documentation"})
public class MemberApiDocumentation {
    private final String baseURL = "/api/v1/member";

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberJobInterestService memberJobInterestService;

    @Test
    @DisplayName("포함된 토큰의 유저 기본 정보 얻기")
    public void memberGet() throws Exception {
        String endpoint = baseURL + "";

        Members member = Members.builder()
                    .uid("서드파티@아이디")
                    .nickname("닉네임")
                    .imagePath("이미지 URL")
                .build();

        given(memberService.findById(any())).willReturn(member);

        this.mockMvc.perform(
                get(endpoint)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("유저정보 업데이트")
    public void memberUpdate() throws Exception {
        String endpoint = baseURL + "";

        MemberDTO memberDTO = MemberDTO.builder()
                .nickname("닉네임")
                .build();

        given(jwtTokenProvider.getUidFromHeader(any())).willReturn(null);

        given(memberService.updateNotNullPropsOf(any())).willReturn(
                Members.builder()
                        .uid("유저의 UID")
                        .nickname(memberDTO.getNickname())
                        .imagePath("프로필 사진 이미지 URL")
                        .build()
        );

        this.mockMvc.perform(
                post(endpoint)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberDTO))).andDo(print());
    }

    @Test
    @DisplayName("닉네임 중복 확인")
    public void nicknameDuplicity() throws Exception {
        String endpoint = baseURL + '/' + "/nickname/duplicity";

        MemberDTO memberDTO = MemberDTO.builder()
                    .nickname("닉네임")
                .build();

        given(memberService.isNicknameExist(memberDTO.getNickname())).willReturn(true);

        this.mockMvc.perform(
                post(endpoint)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(memberDTO))).andDo(print());
    }

    @Test
    @DisplayName("닉네임 업데이트")
    public void nicknameUpdate() throws Exception {
        String endpoint = baseURL + '/' + "/nickname";

        MemberDTO memberDTO = MemberDTO.builder()
                .nickname("닉네임")
                .build();

        given(jwtTokenProvider.getUidFromHeader(any())).willReturn(null);

        given(memberService.isNicknameExist(memberDTO.getNickname())).willReturn(false);

        given(memberService.updateNotNullPropsOf(any())).willReturn(
                Members.builder()
                        .uid("유저의 UID")
                        .nickname(memberDTO.getNickname())
                        .imagePath("프로필 사진 이미지 URL")
                    .build()
        );

        this.mockMvc.perform(
                put(endpoint)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(memberDTO))).andDo(print());
    }

    @Test
    @DisplayName("프로필 사진 업데이트")
    public void imageUploadOrUpdate() throws Exception {
        String endpoint = baseURL + '/' + "image";

        MemberDTO memberDTO = MemberDTO.builder()
                .nickname("닉네임")
                .build();

        given(jwtTokenProvider.getUidFromHeader(any())).willReturn(null);

        given(memberService.updateProfilePicture(any(), any())).willReturn(
                Members.builder()
                        .uid("유저의 UID")
                        .nickname(memberDTO.getNickname())
                        .imagePath("업데이트, 혹은 업로드한 프로필 사진 이미지 URL")
                        .build()
        );

        this.mockMvc.perform(
                fileUpload(endpoint).file("multipartImageFile", "멀티파트 파일바이트".getBytes())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                    .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)).andDo(print());

    }

    @Test
    @DisplayName("저장된 (현재 + 이전) 프로필 사진 경로들 불러오기")
    public void imagesGet() throws Exception {
        String endpoint = baseURL + '/' + "images";

        given(jwtTokenProvider.getUidFromHeader(any())).willReturn(null);

        String[] paths = {"이미지 경로 1", "이미지 경로 2"};
        given(memberService.getSavedImagePaths(null)).willReturn(
                Arrays.asList(paths)
        );

        this.mockMvc.perform(
                get(endpoint)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print());
    }

    @Test
    @DisplayName("저장된 프로필 사진 삭제하기")
    public void imagesDelete() throws Exception {
        String endpoint = baseURL + '/' + "images";

        given(jwtTokenProvider.getUidFromHeader(any())).willReturn("");

        String[] paths = {"이미지 경로 1", "이미지 경로 2"};
        given(memberService.getSavedImagePaths(any())).willReturn(
                Arrays.asList(paths)
        );

        this.mockMvc.perform(
                delete(endpoint)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paths))).andDo(print());
    }

    @Test
    @DisplayName("사용자의 직군/직무 불러오기")
    public void jobInterestsGet() throws Exception {
        String endpoint = baseURL + '/' + "jobInterests";

        given(jwtTokenProvider.getUidFromHeader(any())).willReturn("");

        JobInterest jobGroup_development = JobInterest.builder().id(1L).name("개발").jobInterestType(JobInterestType.JOB_GROUP).build();
        JobInterest jobGroup_design = JobInterest.builder().id(2L).name("디자인").jobInterestType(JobInterestType.JOB_GROUP).build();

        given(memberJobInterestService.getJobGroupList("")).willReturn(
          new ArrayList<JobInterest>() {{
              add(jobGroup_development);
              add(jobGroup_design);
          }}
        );

        given(memberJobInterestService.getJobInterestsByUidAndJobGroup("", jobGroup_development.getId())).willReturn(
            new ArrayList<JobInterest>() {{
                add(JobInterest.builder().id(3L).name("웹 프로그래머").jobInterestType(JobInterestType.JOB_INTEREST).jobGroup(jobGroup_development).build());
                add(JobInterest.builder().id(4L).name("게임 프로그래머").jobInterestType(JobInterestType.JOB_INTEREST).jobGroup(jobGroup_development).build());
                add(JobInterest.builder().id(5L).name("UX/UI 디자이너").jobInterestType(JobInterestType.JOB_INTEREST).jobGroup(jobGroup_design).build());
            }}
        );

        given(memberJobInterestService.getJobInterestsByUidAndJobGroup("", jobGroup_design.getId())).willReturn(
                new ArrayList<JobInterest>() {{
                    add(JobInterest.builder().id(5L).name("UX/UI 디자이너").jobInterestType(JobInterestType.JOB_INTEREST).jobGroup(jobGroup_design).build());
                }}
        );

        this.mockMvc.perform(
                get(endpoint)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                    .contentType(MediaType.APPLICATION_JSON)).andDo(print());
    }


    @Test
    @DisplayName("사용자의 직군/직무 추가하기")
    public void jobInterestAdd() throws Exception {
        String endpoint = baseURL + '/' + "jobInterest";

        JobInterest jobGroup_design = JobInterest.builder().id(2L).name("디자인").jobInterestType(JobInterestType.JOB_GROUP).build();
        JobInterest jobInterest = JobInterest.builder().id(5L).name("UX/UI 디자이너").jobInterestType(JobInterestType.JOB_INTEREST).jobGroup(jobGroup_design).build();
        JobInterestDTO jobInterestDTO = new JobInterestDTO(jobInterest);

        given(jwtTokenProvider.getUidFromHeader(any())).willReturn("");

        given(memberJobInterestService.addJobInterestForUid(any(), any())).willReturn(
                MemberJobInterest.builder()
                            .id(1L)
                            .member(Members.builder().uid("사용자 UID").build())
                            .jobInterest(jobInterest)
                        .build()
        );

        this.mockMvc.perform(
                put(endpoint)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jobInterestDTO))).andDo(print());
    }

    @Test
    @DisplayName("사용자의 직군/직무 리스트 추가하기")
    public void jobInterestsAdd() throws Exception {
        String endpoint = baseURL + '/' + "jobInterests";

        JobInterestIdDTO jobInterestIdDTO = new JobInterestIdDTO(Arrays.asList(3L, 4L));

        given(jwtTokenProvider.getUidFromHeader(any())).willReturn("");

        given(memberJobInterestService.addJobInterestListForUid(any(), any())).willReturn(2);

        this.mockMvc.perform(
                put(endpoint)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {ACCESS_TOKEN}")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jobInterestIdDTO))).andDo(print());
    }

    @BeforeEach
    public void setUp(WebApplicationContext context, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{class-name}/{method-name}",
                                preprocessRequest(modifyUris().host("erooja.eroom.com").port(20000), prettyPrint()),
                                preprocessResponse(prettyPrint())))
                .build();
    }
}
