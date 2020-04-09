package com.eroom.erooja.common.exception;

public class GoalNotFoundException  extends RuntimeException {
    public GoalNotFoundException(String msg) {
        super(msg);
    }

    public GoalNotFoundException() {
        super();
    }
}
