package com.ssarl.sbtformanager;

import android.support.annotation.NonNull;

public class Answer implements Comparable<Answer> {
    public int questionNum;
    public int sentTime;
    public int value;

    public Answer(){
        questionNum = 0;
        sentTime =0 ;
        value = 0;
    }
    public Answer(int questionNum, int sentTime, int value){
        this.questionNum = questionNum;
        this.sentTime = 0;
        this.value = value;
    }

    @Override
    public int compareTo(@NonNull Answer answer) {
        int compareTime = ((Answer)answer).sentTime;

        return this.sentTime - compareTime;
    }
}
