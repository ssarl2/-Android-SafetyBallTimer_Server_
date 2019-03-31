package com.ssarl.sbtformanager;

public class Answer {
    public  String questionNum;
    public  String sentTime;
    public  String value;

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
}
