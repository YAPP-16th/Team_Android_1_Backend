package com.eroom.erooja.features.membergoal.controller;

import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.membergoal.dto.ExistGoalJoinRequestDTO;
import com.eroom.erooja.features.membergoal.service.MemberGoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/membergoal")
@Controller
public class MemberGoalContoller {
    private final MemberGoalService memberGoalService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity joinExistGoal(@RequestBody ExistGoalJoinRequestDTO goalJoinRequest,
                                        @RequestHeader(name = HttpHeaders.AUTHORIZATION) String header){
        String uid = jwtTokenProvider.getUidFromHeader(header);
        MemberGoal memberGoal = memberGoalService.joinGoal(
                uid,
                goalJoinRequest.getGoalId(),
                goalJoinRequest.getEndDt(),
                GoalRole.PARTICIPANT);

        return new ResponseEntity(memberGoal, HttpStatus.CREATED);
    }

    @PostMapping("/new")
    public ResponseEntity joinNewGoal(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String header){
        return new ResponseEntity(null, HttpStatus.CREATED);
    }


    @GetMapping("/goal")
    public ResponseEntity getGoalJoinListByUid(@RequestParam String uid){
        return new ResponseEntity(null, HttpStatus.OK);
    }

    @GetMapping("/member")
    public ResponseEntity getMemberListByGoalId(@RequestParam Long goalId){
        return new ResponseEntity(null, HttpStatus.OK);
    }
}
