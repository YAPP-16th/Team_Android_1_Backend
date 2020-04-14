package com.eroom.erooja.domain.specification;

import com.eroom.erooja.features.goal.dto.GoalSearchRequestDTO;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class GoalCriteria {
    private String field;
    private String keyword;
    private LocalDateTime fromDt;
    private LocalDateTime toDt;
    private Set<Long> jobInterestIdSet;
    private PageRequest pageRequest;

    public static GoalCriteria of(GoalSearchRequestDTO searchRequest) throws UnsupportedEncodingException {
        return GoalCriteria.builder()
                    .field(searchRequest.getGoalFilterBy() == null ? null : searchRequest.getGoalFilterBy().getField())
                    .keyword(searchRequest.getKeyword() == null ? null : URLDecoder.decode(searchRequest.getKeyword(), "UTF-8"))
                    .fromDt(searchRequest.getStartDt())
                    .toDt(searchRequest.getEndDt())
                    .jobInterestIdSet(searchRequest.getJobInterestIds())
                    .pageRequest(pageRequestOf(searchRequest))
                .build();
    }

    private static PageRequest pageRequestOf(GoalSearchRequestDTO searchRequest) {
        return PageRequest.of(
                searchRequest.getPage(), searchRequest.getSize(),
                Sort.by(searchRequest.getDirection(), searchRequest.getGoalSortBy().getField()));
    }
}
