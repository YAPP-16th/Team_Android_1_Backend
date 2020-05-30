package com.eroom.erooja.task;

import com.eroom.erooja.domain.enums.AlarmType;
import com.eroom.erooja.domain.model.MemberGoal;
import com.eroom.erooja.features.alarm.constant.AlarmConstant;
import com.eroom.erooja.features.alarm.dto.InsertMessageDTO;
import com.eroom.erooja.features.alarm.service.AlarmService;
import com.eroom.erooja.features.goal.service.GoalService;
import com.eroom.erooja.features.membergoal.service.MemberGoalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MemberGoalInspectTask {
    private static final Logger logger = LoggerFactory.getLogger(MemberGoalInspectTask.class);

    private final GoalService goalService;
    private final MemberGoalService memberGoalService;
    private final AlarmService alarmService;

    @Transactional
    @Scheduled(cron = "0 0/30 0 * * *")
    public void inspectAndAlarm() {
        logger.info("Checking MemberGoals...");

        goalService.updateFinishedGoalToEnd();
        alarmEndedMemberGoal();

        logger.info("Done");
    }

    private void alarmEndedMemberGoal() {
        List<MemberGoal> memberGoals = memberGoalService.getAllEndedYesterday();
        memberGoals.forEach((memberGoal) -> {
            alarmService.insertMessage(InsertMessageDTO.builder()
                    .messageType(AlarmType.GOAL_TERMINATED)
                    .receiverUid(memberGoal.getUid())
                    .goalId(memberGoal.getGoalId())
                    .title(AlarmConstant.TITLE_GOAL_TERMINATED)
                    .isChecked(false)
                    .content(memberGoal.getGoal().getTitle()).build()
            );
        });
    }

    public MemberGoalInspectTask(GoalService goalService, MemberGoalService memberGoalService, AlarmService alarmService) {
        this.goalService = goalService;
        this.memberGoalService = memberGoalService;
        this.alarmService = alarmService;
    }
}
