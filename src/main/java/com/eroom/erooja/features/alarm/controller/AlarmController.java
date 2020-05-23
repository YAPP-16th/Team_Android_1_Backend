package com.eroom.erooja.features.alarm.controller;

import com.eroom.erooja.domain.model.Alarm;
import com.eroom.erooja.features.alarm.service.AlarmService;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
@Controller
public class AlarmController {
    private final AlarmService alarmService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping
    public ResponseEntity getMessageAll(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String header,
                                        Pageable pageable) {
        String uid = jwtTokenProvider.getUidFromHeader(header);
        Page<Alarm> messages = alarmService.getMessageAllByUid(uid, pageable);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/unchecked")
    public ResponseEntity getMessageUnchecked(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String header,
                                              Pageable pageable) {
        String uid = jwtTokenProvider.getUidFromHeader(header);
        Page<Alarm> messages = alarmService.getMessageUncheckedByUid(uid, pageable);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{alarmId}")
    public ResponseEntity changeStateToChecked(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String header,
                                               @PathVariable("alarmId") Long alarmId) {
        String uid = jwtTokenProvider.getUidFromHeader(header);
        Alarm message = alarmService.changeStateToChecked(uid, alarmId);
        return ResponseEntity.ok(message);
    }
}
