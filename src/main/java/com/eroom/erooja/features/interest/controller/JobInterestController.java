package com.eroom.erooja.features.interest.controller;

import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.features.interest.dto.JobGroupAndInterestsDTO;
import com.eroom.erooja.features.interest.dto.JobInterestDTO;
import com.eroom.erooja.features.interest.service.JobInterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobInterestController {
    private final JobInterestService jobInterestService;

    @GetMapping("/jobGroup")
    public ResponseEntity getJobGroups() {
        List<JobInterest> jobGroups = jobInterestService.findJobGroups();

        List<JobInterestDTO> jobInterestDTOList
                = jobGroups.stream()
                    .map(JobInterestDTO::new)
                    .collect(Collectors.toList());

        return ResponseEntity.ok(jobInterestDTOList);
    }

    @GetMapping("/jobGroup/{id}")
    public ResponseEntity getJobGroup(@PathVariable Long id) {
        JobInterest jobGroup = jobInterestService.findById(id);
        List<JobInterest> jobInterests = jobInterestService.findByJobGroup_id(id);

        JobGroupAndInterestsDTO jobGroupAndInterestsDTO
                = new JobGroupAndInterestsDTO(jobGroup, jobInterests);

        return ResponseEntity.ok(jobGroupAndInterestsDTO);
    }


    @GetMapping("/jobInterest/{id}")
    public ResponseEntity getOne(@PathVariable Long id) {
        JobInterest jobInterest = jobInterestService.findById(id);

        return ResponseEntity.ok(new JobInterestDTO(jobInterest));
    }
}
