package com.eroom.erooja.features.goal.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateGoalRequestDTO {
    @Length(max=50, message = "목표명은 50자 이내만 입력")
    private String title;
    private String description;

    @JsonIgnore
    public Boolean isTitleChanged(){
        if(this.title == null || this.title.isEmpty())
            return false;
        return true;
    }

    @JsonIgnore
    public Boolean isDescriptionChanged(){
        if(this.description == null || this.description.isEmpty())
            return false;
        return true;
    }
}
