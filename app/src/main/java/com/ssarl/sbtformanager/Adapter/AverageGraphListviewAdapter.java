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
    private SeekBar seekBar;
    private TextView seekValue;

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
        seekBar = (SeekBar) convertView.findViewById(R.id.seekBarGraph);
        seekValue = (TextView) convertView.findViewById(R.id.seekText);


        title.setText(mAverageGraphitems.get(position).getTitle());
        seekBar.setProgress(mAverageGraphitems.get(position).getSeekBar());

        // working function that view follows thumb of seekbar  view가 시크바의 thumb 따라다니게 만드는 함수
        int padding = seekBar.getPaddingLeft() + seekBar.getPaddingRight();
        int sPos = seekBar.getLeft() + seekBar.getPaddingLeft();
        int xPos = (seekBar.getWidth() - padding) * seekBar.getProgress() / seekBar.getMax() + sPos - (seekValue.getWidth() / 2);
        seekValue.setX(xPos);
        seekValue.setText(String.valueOf(seekBar.getProgress()));

        seekBar.setEnabled(false);

        return convertView;
    }
}
