package com.eroom.erooja.features.interest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JobInterestIdDTO {
    private List<Long> ids = new ArrayList<>();
}
