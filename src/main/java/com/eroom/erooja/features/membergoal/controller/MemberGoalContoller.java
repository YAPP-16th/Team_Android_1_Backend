package com.eroom.erooja.features.membergoal.controller;

import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.domain.model.Members;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.features.goal.service.GoalService;
import com.eroom.erooja.features.member.service.MemberJobInterestService;
import com.eroom.erooja.features.membergoal.dto.*;
import com.eroom.erooja.features.membergoal.service.MemberGoalService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.eroom.erooja.common.constants.ErrorEnum.GOAL_JOIN_ALREADY_EXIST;

@RequiredArgsConstructor
@RequestMapping("/api/v1/membergoal")
@Controller
public class MemberGoalContoller {
    private static final Logger logger = LoggerFactory.getLogger(MemberGoalContoller.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberJobInterestService memberJobInterestService;
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

        MemberGoal memberGoal = null;
        if(memberGoalService.isAlreadyExistJoin(uid, goalJoinRequest.getGoalId())){
            GoalRole originRole = memberGoalService.getGoalRole(uid,goalJoinRequest.getGoalId());
            memberGoal = memberGoalService.joinExistGoal(uid, originRole, goalJoinRequest);
        }else{
            memberGoal = memberGoalService.joinExistGoal(uid, GoalRole.PARTICIPANT, goalJoinRequest);
        }

        return new ResponseEntity(memberGoal, HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping("/{goalId}")
    public ResponseEntity getMembers(@PathVariable Long goalId, Pageable pageable) {
        MemberPageDTO memberPageDTO = memberGoalService.getMembersAllByGoalId(goalId, pageable);
        return ResponseEntity.ok(memberPageDTO);
    }

    @GetMapping
    public ResponseEntity getGoalJoinListByUid(GoalJoinListRequestDTO goalJoinListRequestDTO) {
        Page<GoalJoinMemberDTO> memberGoalDTOPage
                = (goalJoinListRequestDTO.isEnd()) ?
                memberGoalService.getEndedGoalJoinPageByUid(
                        goalJoinListRequestDTO.getUid(),
                        goalJoinListRequestDTO.getPageable())
                :
                memberGoalService.getGoalJoinPageByUid(
                        goalJoinListRequestDTO.getUid(),
                        goalJoinListRequestDTO.getPageable());

        return ResponseEntity.ok(memberGoalDTOPage);
    }

    @GetMapping("/{goalId}/todo")
    public ResponseEntity getJoinTodoListByGoalId(Pageable pageable, @PathVariable Long goalId) {
        Page<GoalJoinTodoDto> goalJoinTodoDtoPage = memberGoalService.getJoinTodoListByGoalId(goalId, pageable);
        return new ResponseEntity(goalJoinTodoDtoPage, HttpStatus.OK);
    }

    @GetMapping("{goalId}/count")
    public ResponseEntity countGoalJoinByGoalId(@PathVariable Long goalId) {
        int count = memberGoalService.countGoalJoinByGoalId(goalId);
        return ResponseEntity.status(HttpStatus.OK).body(count);
    }

    @GetMapping("{goalId}/info")
    public ResponseEntity getJoinOwnInfo(@PathVariable Long goalId,
                                         @RequestHeader(name = HttpHeaders.AUTHORIZATION) String header) {
        String uid = jwtTokenProvider.getUidFromHeader(header);
        MemberGoal memberGoal = memberGoalService.getGoalJoinByUidAndGoalId(uid, goalId);

        return ResponseEntity.status(HttpStatus.OK).body(memberGoal);
    }

    @PutMapping("/{goalId}")
    public ResponseEntity updateGoalJoin(@PathVariable Long goalId,
                                         @RequestBody @Valid UpdateJoinRequestDTO updateGoalJoinRequest,
                                         @RequestHeader(name = HttpHeaders.AUTHORIZATION) String header,
                                         Errors errors) {
        if (errors.hasErrors()) {
            logger.info("error : {}", errors.getFieldError().getDefaultMessage());
            return new ResponseEntity(errors.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        String uid = jwtTokenProvider.getUidFromHeader(header);
        MemberGoal changedMemberGoal = null;

        if (updateGoalJoinRequest.getChangedIsEnd())
            changedMemberGoal = memberGoalService.changeJoinToEnd(uid, goalId);
        else
            changedMemberGoal = memberGoalService.againJoin(updateGoalJoinRequest, uid, goalId);

        return ResponseEntity.status(HttpStatus.OK).body(changedMemberGoal);
    }
}

