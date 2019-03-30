package com.ssarl.sbtformanager;

import java.util.Random;

public class ExampleActivity {
    private int data;
    private int[] data_array;

    public ExampleActivity(){
        data = 20;
        data_array = new int[24];
    }
    public void init(){
        //Random rnd = new Random();
       for(int i=1; i<=24; i++){
           // data = 10;
            data_array[i-1] = data;
        }
    }
    public int getData(int position){
        if(data_array != null){
            return data_array[position-1];
        }
        else{
            return 0;
        }
    }

}
