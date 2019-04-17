package com.ssarl.sbtformanager;

import java.io.Serializable;
import java.util.ArrayList;

public class Answers implements Serializable {

    public ArrayList<EachValue> eachValue;
    public int que_num;
    public int total_value;
    public int count;

    public Answers(){
        eachValue = new ArrayList<EachValue>();
        que_num = 0;
        total_value = 0;
        count = 0;
    }

    public void addItem(EachValue data){
        eachValue.add(data);
    }
}
