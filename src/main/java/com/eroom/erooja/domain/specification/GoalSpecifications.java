package com.eroom.erooja.domain.specification;

import com.eroom.erooja.domain.model.Goal;
import com.eroom.erooja.domain.field.Goal_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GoalSpecifications implements Specification<Goal> {

    private GoalCriteria goalCriteria;

    @Override
    public Predicate toPredicate(Root<Goal> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        List<Specification<Goal>> andSpec = new ArrayList<>();

        if (goalCriteria.getFromDt() != null) {
            andSpec.add(isEndDtGreaterThen(goalCriteria.getFromDt()));
        }

        if (goalCriteria.getToDt() != null) {
            andSpec.add(isStartDtLessThen(goalCriteria.getToDt()));
        }

        if (goalCriteria.getJobInterestIdSet() != null && goalCriteria.getJobInterestIdSet().size() > 0) {
            andSpec.add(isJobInterestIn(goalCriteria.getJobInterestIdSet()));
        }

        if (goalCriteria.getField() != null && !StringUtils.isEmpty(goalCriteria.getKeyword())) {
            andSpec.add(like(goalCriteria.getField(), goalCriteria.getKeyword().trim()));
        }

        return cb.and(andSpec.stream()
                    .map(spec -> spec.toPredicate(root, criteriaQuery, cb))
                    .toArray(Predicate[]::new));
    }

    private Specification<Goal> like(String field, String keyword) {
        return (root, query, cb) -> cb.and(cb.like(root.get(field), "%" + keyword + "%"));
    }

    private Specification<Goal> isJobInterestIn(Set<Long> jobInterestIdSet) {
        return (root, query, cb) -> {
            Predicate pred = root.join("goalJobInterests", JoinType.INNER)
                    .get("jobInterest")
                    .get("id")
                    .in(jobInterestIdSet);
            query.distinct(true);
            return pred;
        };

    }

    private Specification<Goal> isStartDtLessThen(LocalDateTime toDt) {
        return (root, query, cb) -> cb.lessThan(root.get(Goal_.startDt), toDt);
    }

    private Specification<Goal> isEndDtGreaterThen(LocalDateTime fromDt) {
        return (root, query, cb) -> cb.greaterThan(root.get(Goal_.endDt), fromDt);
    }

    public GoalSpecifications(GoalCriteria goalCriteria) {
        this.goalCriteria = goalCriteria;
    }
}
