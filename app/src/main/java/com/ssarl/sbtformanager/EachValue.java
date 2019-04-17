package com.ssarl.sbtformanager;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * Created by Wonhak on 2019-04-02.
 */

public class EachValue implements Serializable, Comparable<EachValue> {

    public String sentTime;
    public int value;

    public EachValue(){
        sentTime = "empty";
        value = 0;
    }


    @Override
    public int compareTo(EachValue eachValue) {

        return sentTime.compareTo(eachValue.sentTime);
    }
}
