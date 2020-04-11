package com.eroom.erooja.features.interest;

import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.features.interest.service.JobInterestService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Profile({"test"}) @ActiveProfiles({"test"})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobInterestApiTest {
    private static final Logger logger = LoggerFactory.getLogger(JobInterestApiTest.class);
    private final String INTEREST_BASE_END_POINT = "/api/v1/jobInterest";
    private final String GROUP_BASE_END_POINT = "/api/v1/jobGroup";

    private final MockMvc mockMvc;

    private final JobInterestService jobInterestService;


    @Test
    @Transactional
    @DisplayName("직군 리스트를 불러올 수 있다.")
    public void getAllJobGroups() throws Exception {
        jobInterestService.addJobGroup("개발");
        jobInterestService.addJobGroup("디자인");

        mockMvc.perform(
                    get(GROUP_BASE_END_POINT)
                        .accept(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("$[0].name", is("개발")))
                .andExpect(jsonPath("$[1].name", is("디자인")));
    }

    @Test
    @Transactional
    @DisplayName("아이디로 직군을 검색하면 직군과 그에 따른 직무 리스트를 가져올 수 있다.")
    public void getJobGroupWithJobInterests() throws Exception {
        JobInterest jobGroup = jobInterestService.addJobGroup("개발");
        jobInterestService.addJobInterest(jobGroup.getId(), "웹 프로그래머");
        jobInterestService.addJobInterest(jobGroup.getId(), "안드로이드");
        jobInterestService.addJobInterest(jobGroup.getId(), "IOS");
        jobInterestService.addJobInterest(jobGroup.getId(), "서버");
        jobInterestService.addJobInterest(jobGroup.getId(), "프론트엔드");
        jobInterestService.addJobInterest(jobGroup.getId(), "Data Engineer");

        mockMvc.perform(
                get(GROUP_BASE_END_POINT + "/" + jobGroup.getId())
                        .accept(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("name", is("개발")))
                .andExpect(jsonPath("jobInterests[0].name", is("웹 프로그래머")))
                .andExpect(jsonPath("jobInterests[1].name", is("안드로이드")))
                .andExpect(jsonPath("jobInterests[2].name", is("IOS")))
                .andExpect(jsonPath("jobInterests[3].name", is("서버")))
                .andExpect(jsonPath("jobInterests[4].name", is("프론트엔드")))
                .andExpect(jsonPath("jobInterests[5].name", is("Data Engineer")));
    }

    @Test
    @Transactional
    @DisplayName("아이디로 직무다 정보를 가져올 수 있다.")
    public void getJobInterest() throws Exception {
        JobInterest jobGroup = jobInterestService.addJobGroup("디자인");
        JobInterest jobInterest = jobInterestService.addJobInterest(jobGroup.getId(), "UX 디자인");

        mockMvc.perform(
                get(INTEREST_BASE_END_POINT + "/" + jobInterest.getId())
                        .accept(MediaType.APPLICATION_JSON + ";charset=UTF-8")
                        .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(jsonPath("name", is("UX 디자인")));
    }
}
