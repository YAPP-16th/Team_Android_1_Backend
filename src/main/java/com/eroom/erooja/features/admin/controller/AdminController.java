package com.eroom.erooja.features.admin.controller;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.features.interest.service.JobInterestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {
    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final JobInterestService jobInterestService;

    @PutMapping("/setJobInterests")
    public ResponseEntity setJobInterests() {
        jobInterestService.setUpDefaultJobInterests();
        return ResponseEntity.ok(true);
    }

    @GetMapping("/ping")
    public ResponseEntity ping() {
        logger.error("Admin pong... 에러를 일으킵니다.");
        throw new EroojaException(ErrorEnum.ETC);
    }
}
