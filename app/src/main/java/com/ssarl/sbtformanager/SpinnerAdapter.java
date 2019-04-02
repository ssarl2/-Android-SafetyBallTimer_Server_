package com.ssarl.sbtformanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Wonhak on 2019-04-02.
 */

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    List<Integer> data;
    LayoutInflater inflater;
    int current_que_num;

    public SpinnerAdapter(Context context, List<Integer> data){{
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }}

    @Override
    public int getCount() {
        if(data != null) return data.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view==null) {
            view = inflater.inflate(R.layout.spinner_spinner1_normal, parent, false);
        }

        if(data!=null){
            //데이터세팅
            int que_num = data.get(position);
            current_que_num = que_num;
            ((TextView)view.findViewById(R.id.spinnerText)).setText(Integer.toString(que_num));
        }

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.spinner_spinner1_dropdown, parent, false);
        }

        //데이터세팅
        int que_num = data.get(position);
        ((TextView)convertView.findViewById(R.id.spinnerText)).setText(Integer.toString(que_num));

        return convertView;
    }

    public int getCurrentQuestionNumber(){
        int current_data = current_que_num;
        return current_data;
    }
}
