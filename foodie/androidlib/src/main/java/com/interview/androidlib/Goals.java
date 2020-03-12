package com.interview.androidlib;

public class Goals {
    Boolean isAGoal;
    String goal;

    public Goals(Boolean isAGoal, String goal) {
        this.isAGoal = isAGoal;
        this.goal = goal;
    }

    public Boolean isAGoal() {
        return isAGoal;
    }

    public String getGoal() {
        return goal;
    }

    public void setAGoal(Boolean AGoal) {
        isAGoal = AGoal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
