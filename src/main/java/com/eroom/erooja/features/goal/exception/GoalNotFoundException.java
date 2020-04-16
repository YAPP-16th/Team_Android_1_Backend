package com.eroom.erooja.features.goal.exception;

import com.eroom.erooja.common.constants.ErrorEnum;
import com.eroom.erooja.common.exception.EroojaException;

public class GoalNotFoundException  extends EroojaException {
    public GoalNotFoundException(ErrorEnum errorEnum) {
        super(errorEnum);
    }
}
