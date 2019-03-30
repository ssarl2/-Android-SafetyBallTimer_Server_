package com.ssarl.sbtformanager;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class ListviewActivity extends BaseAdapter {
    private ExampleActivity example_data = null;
    private DataPoint[] dataPoints = null;
    private List<Question> question_list = null;
    private Context context = null;
    private int layout;

    public ListviewActivity(Context c, int layout, List<Question> list){
        example_data = new ExampleActivity();
        this.context = c;
        this.layout = layout;
        this.question_list = list;
        Log.d("인덱스", Integer.toString(question_list.size()));
    }

    @Override
    public int getCount() {
        return question_list.size();
    }

    @Override
    public Object getItem(int position) {
        return example_data.getData(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, parent, false);
        }

        example_data.init();

        GraphView graph = (GraphView) convertView.findViewById(R.id.graph);
        dataPoints = new DataPoint[24];
        for(int i=1; i<=24; i++){
            int xPos = i;
            int yPos = example_data.getData(i);
            dataPoints[i-1] = new DataPoint(xPos, yPos);
        }

        //PointsGraphSeries<DataPoint> series = new PointsGraphSeries<DataPoint>(dataPoints);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(24);
        graph.getViewport().setMinY(1);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        series.setColor(Color.BLUE);
        //series.setShape(PointsGraphSeries.Shape.POINT);
        //series.setSize(10);
        graph.addSeries(series);

        return convertView;
    }
}
