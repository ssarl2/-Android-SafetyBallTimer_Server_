package com.ssarl.sbtformanager.Object;

import java.io.Serializable;

/**
 * Created by Wonhak on 2019-04-02.
 */

public class EachValue implements Serializable, Comparable<EachValue> {

    private String sentTime;
    private int value;

    EachValue(){
        sentTime = "empty";
        value = 0;
    }

    public String getSentTime() {
        return sentTime;
    }

    public int getValue() {
        return value;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int compareTo(EachValue eachValue) {
        return sentTime.compareTo(eachValue.sentTime);
    }
}
