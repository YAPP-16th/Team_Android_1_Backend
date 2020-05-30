package com.eroom.erooja.features.admin.controller;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;
import com.eroom.erooja.domain.enums.AuthProvider;
import com.eroom.erooja.domain.model.JobInterest;
import com.eroom.erooja.features.interest.service.JobInterestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {
    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);
    private final JobInterestService jobInterestService;

    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("SqlWithoutWhere")
    @GetMapping("/dbReset")
    @Transactional
    public ResponseEntity<?> dbReset() {
        logger.warn("[주의] DB 리셋 요청 확인. 어드민 계정을 포함한 모든 데이터를 삭제합니다.");

        em.createNativeQuery("DELETE FROM alarm").executeUpdate();
        em.createNativeQuery("DELETE FROM todo").executeUpdate();
        em.createNativeQuery("DELETE FROM goal_job_interest").executeUpdate();
        em.createNativeQuery("DELETE FROM member_goal").executeUpdate();
        em.createNativeQuery("DELETE FROM goal").executeUpdate();
        em.createNativeQuery("DELETE FROM member_job_interest").executeUpdate();
        em.createNativeQuery("DELETE FROM member_auth_authorities").executeUpdate();
        em.createNativeQuery("DELETE FROM members").executeUpdate();
        em.createNativeQuery("DELETE FROM member_auth").executeUpdate();
        em.createNativeQuery("DELETE FROM job_interest WHERE job_group_id is not null").executeUpdate();
        em.createNativeQuery("DELETE FROM job_interest WHERE job_group_id is null").executeUpdate();

        em.createNativeQuery("UPDATE hibernate_sequence SET next_val = 1").executeUpdate();

        em.createNativeQuery("INSERT INTO `member_auth` (" +
                        "`uid`, " +
                        "`create_dt`, " +
                        "`update_dt`, " +
                        "`auth_provider`, " +
                        "`is_account_non_expired`, " +
                        "`is_account_non_locked`, " +
                        "`is_additional_info_needed`, " +
                        "`is_credentials_non_expired`, " +
                        "`is_enabled`, " +
                        "`is_third_party`, " +
                        "`password`, " +
                        "`third_party_user_info`)" +
                    " VALUES('EROOJA@1', :nowDate, :nowDate, :authProvider, 1, 1, 0, 1, 1, 0, 'erooja20!', '')")
                .setParameter("authProvider", AuthProvider.EROOJA.toString())
                .setParameter("nowDate", LocalDateTime.now()).executeUpdate();

        em.createNativeQuery("INSERT INTO `member_auth_authorities` (`member_auth_uid`, `authorities`)" +
                    " VALUES ('EROOJA@1', 'ROLE_ADMIN')," +
                            "('EROOJA@1', 'ROLE_USER')")
                .executeUpdate();

        em.createNativeQuery("INSERT INTO `members` (" +
                        "`create_dt`, " +
                        "`update_dt`, " +
                        "`image_path`, " +
                        "`nickname`, " +
                        "`member_auth_uid`) " +
                    " VALUES(:nowDate, :nowDate, '', '어드민', 'EROOJA@1')")
                .setParameter("nowDate", LocalDateTime.now()).executeUpdate();

        logger.warn("[주의] 데이터베이스 초기화 완료. 관심직무/직군 셋업을 실행해주십시오.");
        return ResponseEntity.ok(true);
    }

    @PutMapping("/setJobInterests")
    public ResponseEntity setJobInterests() {
        logger.warn("[주의] 기본 관심직무/직군을 셋업합니다.");
        List<JobInterest> jobInterests = jobInterestService.setUpDefaultJobInterests();

        logger.warn("[주의] 기본 관심직무/직군 셋업 완료. {}", jobInterests);
        return ResponseEntity.ok(true);
    }

    @GetMapping("/ping")
    public ResponseEntity ping() {
        logger.warn("에러 ping 요청 확인.");
        logger.warn("pong... 에러를 일으킵니다.");
        throw new EroojaException(ErrorEnum.ETC);
    }
}
