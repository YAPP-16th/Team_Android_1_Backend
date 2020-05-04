package com.eroom.erooja.features.goal.controller;

import com.eroom.erooja.features.goal.exception.GoalNotFoundException;
import com.eroom.erooja.domain.enums.GoalRole;
import com.eroom.erooja.features.auth.jwt.JwtTokenProvider;
import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.specification.GoalCriteria;
import com.eroom.erooja.features.goal.dto.CreateGoalRequestDTO;
import com.eroom.erooja.features.goal.dto.GoalSearchRequestDTO;
import com.eroom.erooja.features.goal.service.GoalService;
import com.eroom.erooja.features.goaljobinterest.service.GoalJobInterestService;
import com.eroom.erooja.features.membergoal.service.MemberGoalService;
import com.eroom.erooja.features.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;


@RequiredArgsConstructor
@RequestMapping("/api/v1/goal")
@RestController
public class GoalController {
    private final GoalService goalService;
    private final GoalJobInterestService goalJobInterestService;
    private final MemberGoalService memberGoalService;
    private final TodoService todoService;
    private final JwtTokenProvider jwtTokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(GoalController.class);

    @GetMapping("/{goalId}")
    ResponseEntity getGoalDetail(@PathVariable("goalId") Long goalId) {
        Goal findGoal;

        try {
            findGoal = goalService.findGoalById(goalId);
        } catch (GoalNotFoundException e) {
            logger.error("error : {}", e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity(findGoal, HttpStatus.OK);
    }

    @GetMapping(value = "/interest/{interestId}")
    ResponseEntity getGoalList(@PathVariable("interestId") Long interestId, Pageable pageable) {
        Page<Goal> goalList = goalService.findGoalListByInterestId(interestId, pageable);
        return new ResponseEntity(goalList, HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity searchGoal(GoalSearchRequestDTO goalSearchRequestDTO,
                              Errors errors) {
        if (errors.hasErrors()) {
            throw new EroojaException(ErrorEnum.GOAL_INVALID_ARGS);
        }

        try {
            GoalCriteria criteria = GoalCriteria.of(goalSearchRequestDTO);
            Page<Goal> goalPage = goalService.search(criteria);

            return ResponseEntity.ok(goalPage);
        } catch (UnsupportedEncodingException e) {
            logger.error("키워드, {} 를 디코드할 수 없습니다.", goalSearchRequestDTO.getKeyword());
            throw new EroojaException(ErrorEnum.GOAL_INVALID_ARGS);
        }
    }

    @PostMapping(produces = "application/json; charset=utf-8")
    ResponseEntity createGoal(@RequestBody @Valid CreateGoalRequestDTO createGoalRequest,
                              @RequestHeader(name = HttpHeaders.AUTHORIZATION) String header
                              ,Errors errors) {
        if (errors.hasErrors()) {
            logger.error("error : {}", errors.getFieldError().getDefaultMessage());
            return new ResponseEntity(errors.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }

        String uid = jwtTokenProvider.getUidFromHeader(header);
        Goal newGoal = goalService.createGoal(createGoalRequest);

        goalJobInterestService.addJobInterestListForGoal(newGoal.getId(),
                createGoalRequest.getInterestIdList());

        memberGoalService.addMemberGoal(uid,
                newGoal.getId(),
                newGoal.getEndDt(),
                GoalRole.OWNER);

        todoService.addTodo(uid, newGoal.getId(), createGoalRequest.getTodoList());

        return new ResponseEntity(newGoal, HttpStatus.CREATED);
    }

}
