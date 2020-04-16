package com.eroom.erooja.common.exception;

public class MemberGoalNotFoundException  extends RuntimeException{
    public MemberGoalNotFoundException(String msg) {
        super(msg);
    }
    public MemberGoalNotFoundException() {
        super();
    }
}
