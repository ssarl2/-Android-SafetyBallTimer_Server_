package com.ssarl.sbtformanager;

import android.support.annotation.NonNull;

public class Answer implements Comparable<Answer> {
    public String questionNum;
    public String sentTime;
    public String value;

    public Answer(){
        questionNum = "empty";
        sentTime = "empty";
        value = "empty";
    }
    public Answer(String questionNum, String sentTime, String value){
        this.questionNum = questionNum;
        this.sentTime = "time";
        this.value = value;
    }

    @Override
    public int compareTo(@NonNull Answer answer) {
        int compareTime = Integer.parseInt(((Answer)answer).sentTime);

        return Integer.parseInt(this.sentTime) - compareTime;
    }
}
