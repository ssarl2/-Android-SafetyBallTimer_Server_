package com.ssarl.sbtformanager.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ssarl.sbtformanager.Object.AverageGraphitem;
import com.ssarl.sbtformanager.R;

import java.util.ArrayList;

public class AverageGraphListviewAdapter extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<AverageGraphitem> mAverageGraphitems = null;
    private int nListCount = 0;

    private TextView title;
    private ProgressBar progressBar;
    private TextView progressBarValue;

    public AverageGraphListviewAdapter(ArrayList<AverageGraphitem> averageGraphitems) {
        mAverageGraphitems = averageGraphitems;
        nListCount = averageGraphitems.size();
    }

    @Override
    public int getCount() {
        return nListCount;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            final Context context = parent.getContext();
            if(inflater == null){
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.custom_listview_graph, parent,false);
        }

        title = (TextView) convertView.findViewById(R.id.titleGraph);
        progressBar = (ProgressBar) convertView.findViewById(R.id.progressBarGraph);
        progressBarValue = (TextView) convertView.findViewById(R.id.progressBarText);


        title.setText(mAverageGraphitems.get(position).getTitle());
        progressBar.setProgress(mAverageGraphitems.get(position).getSeekBar());
        progressBarValue.setText(progressBar.getProgress()+"%");

        return convertView;
    }
}
