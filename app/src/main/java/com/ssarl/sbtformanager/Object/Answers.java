package com.ssarl.sbtformanager.Object;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Answers implements Serializable, Cloneable {

    private ArrayList<EachValue> eachValue;

    private int que_num;
    private int total_value;
    private int count;
    private String question;



    public Answers(){
        eachValue = new ArrayList<>();
    }

    public ArrayList<EachValue> getEachValue() {
        return eachValue;
    }

    public void setEachValue(ArrayList<EachValue> eachValue) {
        this.eachValue.addAll(eachValue);
    }

    public int getQue_num() {
        return que_num;
    }

    public void setQue_num(int que_num) {
        this.que_num = que_num;
    }

    public int getTotal_value() {
        return total_value;
    }

    public void setTotal_value(int total_value) {
        this.total_value = total_value;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
