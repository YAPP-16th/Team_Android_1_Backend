package com.eroom.erooja.documentation.v1;

import com.eroom.erooja.domain.enums.JobInterestType;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.features.interest.service.JobInterestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs(uriHost = "erooja.eroom.com")
@ActiveProfiles({"documentation"})
public class JobInterestApiDocumentation {
    private final String baseURL = "/api/v1";

    private MockMvc mockMvc;

    @MockBean
    private JobInterestService jobInterestService;

    @Test
    @DisplayName("직군 불러오기")
    public void jobGroupsGet() throws Exception {
        String endpoint = baseURL + "/jobGroup";

        JobInterest jobGroup_development = JobInterest.builder().id(1L).name("개발").jobInterestType(JobInterestType.JOB_GROUP).build();
        JobInterest jobGroup_design = JobInterest.builder().id(2L).name("디자인").jobInterestType(JobInterestType.JOB_GROUP).build();

        JobInterest jobInterest01 = JobInterest.builder().id(3L).name("웹 프로그래머").jobInterestType(JobInterestType.JOB_INTEREST).jobGroup(jobGroup_development).build();
        JobInterest jobInterest02 = JobInterest.builder().id(4L).name("게임 프로그래머").jobInterestType(JobInterestType.JOB_INTEREST).jobGroup(jobGroup_development).build();
        JobInterest jobInterest03 = JobInterest.builder().id(5L).name("UX/UI 디자이너").jobInterestType(JobInterestType.JOB_INTEREST).jobGroup(jobGroup_design).build();

        given(jobInterestService.findJobGroups()).willReturn(
                Arrays.asList(
                        jobGroup_development, jobGroup_design,
                        jobInterest01, jobInterest02, jobInterest03
                )
        );

        this.mockMvc.perform(
                get(endpoint).contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("직군과 직무 함께 불러오기")
    public void jobGroupGet() throws Exception {
        String endpoint = baseURL + "/jobGroup/" + 1;

        JobInterest jobGroup_development = JobInterest.builder().id(1L).name("개발").jobInterestType(JobInterestType.JOB_GROUP).build();

        JobInterest jobInterest01 = JobInterest.builder().id(3L).name("웹 프로그래머").jobInterestType(JobInterestType.JOB_INTEREST).jobGroup(jobGroup_development).build();
        JobInterest jobInterest02 = JobInterest.builder().id(4L).name("게임 프로그래머").jobInterestType(JobInterestType.JOB_INTEREST).jobGroup(jobGroup_development).build();

        given(jobInterestService.findById(jobGroup_development.getId())).willReturn(jobGroup_development);
        given(jobInterestService.findByJobGroup_id(jobGroup_development.getId())).willReturn(
                Arrays.asList(jobInterest01, jobInterest02)
        );

        this.mockMvc.perform(
                get(endpoint).contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("직군/직무 단일 조회")
    public void jobInterestGet() throws Exception {
        String endpoint = baseURL + "/jobInterest/" + 3;

        JobInterest jobGroup_development = JobInterest.builder().id(1L).name("개발").jobInterestType(JobInterestType.JOB_GROUP).build();
        JobInterest jobInterest01 = JobInterest.builder().id(3L).name("웹 프로그래머").jobInterestType(JobInterestType.JOB_INTEREST).jobGroup(jobGroup_development).build();

        given(jobInterestService.findById(jobInterest01.getId())).willReturn(jobInterest01);

        this.mockMvc.perform(
                get(endpoint).contentType(MediaType.APPLICATION_JSON));
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
