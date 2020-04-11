package com.eroom.erooja.features.admin.controller;

import com.eroom.erooja.features.interest.service.JobInterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {
    private final JobInterestService jobInterestService;

    @PutMapping("/setJobInterests")
    public ResponseEntity setJobInterests() {
        jobInterestService.setUpDefaultJobInterests();
        return ResponseEntity.ok(true);
    }
}
