package com.eroom.erooja.features.membergoal.controller;

import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.goal.service.GoalService;
import com.eroom.erooja.features.membergoal.dto.GoalJoinListRequestDTO;
import com.eroom.erooja.features.membergoal.dto.GoalJoinMemberDTO;
import com.eroom.erooja.features.membergoal.dto.GoalJoinRequestDTO;
import com.eroom.erooja.features.membergoal.service.MemberGoalService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.stream.Collectors;

import static com.eroom.erooja.common.constants.ErrorEnum.GOAL_JOIN_ALREADY_EXIST;

@RequiredArgsConstructor
@RequestMapping("/api/v1/membergoal")
@Controller
public class MemberGoalContoller {
    private static final Logger logger = LoggerFactory.getLogger(MemberGoalContoller.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberGoalService memberGoalService;
    private final GoalService goalService;


    @PostMapping(produces = "application/json; charset=utf-8")
    public ResponseEntity joinExistGoal(@RequestBody @Valid GoalJoinRequestDTO goalJoinRequest,
                                        @RequestHeader(name = HttpHeaders.AUTHORIZATION) String header,
                                        Errors errors) {
        if (errors.hasErrors()) {
            logger.info("error : {}", errors.getFieldError().getDefaultMessage());
            return new ResponseEntity(errors.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        String uid = jwtTokenProvider.getUidFromHeader(header);

        if (memberGoalService.isAlreadyExistJoin(uid, goalJoinRequest.getGoalId()))
            throw new EroojaException(GOAL_JOIN_ALREADY_EXIST);

        MemberGoal memberGoal = memberGoalService.joinExistGoal(
                uid,
                goalJoinRequest);

        return new ResponseEntity(memberGoal, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity getGoalJoinListByUid(GoalJoinListRequestDTO goalJoinListRequestDTO) {

        Page<MemberGoal> goalJoinPageByUid = memberGoalService.getGoalJoinPageByUid(
                goalJoinListRequestDTO.getUid(),
                goalJoinListRequestDTO.getPageable());

        Page<GoalJoinMemberDTO> memberGoalPage = convertPage2DTO(goalJoinPageByUid);

        return ResponseEntity.ok(memberGoalPage);
    }

    private Page<GoalJoinMemberDTO> convertPage2DTO(Page<MemberGoal> origin) {
        return new PageImpl<>(
                origin.getContent().stream()
                        .map(mg -> GoalJoinMemberDTO.of(mg, goalService.findGoalById(mg.getGoalId())))
                        .collect(Collectors.toList()),
                origin.getPageable(), origin.getTotalElements());
    }

    @GetMapping("/member")
    public ResponseEntity getMemberListByGoalId(@RequestParam Long goalId) {
        return new ResponseEntity(null, HttpStatus.OK);
    }
}
