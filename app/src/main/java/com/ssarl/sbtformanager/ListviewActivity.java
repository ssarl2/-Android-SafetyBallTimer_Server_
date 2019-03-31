package com.ssarl.sbtformanager;


import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.LongDef;
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
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.List;

public class ListviewActivity extends BaseAdapter {
    private DataPoint[] dataPoints = null;
    private ArrayList<Answer> answers = new ArrayList<>();
    private Context context = null;
    private int layout;

    public ListviewActivity(Context c, int layout){
        this.context = c;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return answers.size();
    }

    @Override
    public Object getItem(int position) {
        return answers.get(position);
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
        int sumArr[]=new int[20];
        int count[]=new int [20];
        for(int j=0; j<20;j++)
        {  count[j]=1;
            for(int k=0; k<answers.size();k++)
            {
                if(j==Integer.parseInt(answers.get(k).questionNum)){
                    sumArr[j]=sumArr[j]+Integer.parseInt(answers.get(k).value);
                    count[j]=count[j]+1;
                }
            }
            Log.d("카운트",Integer.toString(count[j]));
        }


        GraphView graph = (GraphView) convertView.findViewById(R.id.graph);

        dataPoints = new DataPoint[20];
        for(int i=0; i<20; i++){
            int xPos = i;
            int yPos = sumArr[i];
            dataPoints[i] = new DataPoint(xPos, yPos);
            Log.d("문제번호"+i,Integer.toString(xPos));
            Log.d("합산"+i,Integer.toString(yPos));

        }

        //PointsGraphSeries<DataPoint>series=new PointsGraphSeries<DataPoint>(dataPoints);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
       graph.getViewport().setMinX(1);
       graph.getViewport().setMaxX(20);
       graph.getViewport().setMinY(1);
        graph.getViewport().setMaxY(100);
       graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
       //series.setCustomPaint(paint);
        series.setColor(Color.BLUE);
        graph.addSeries(series);

        return convertView;
    }

    public void addItem(Answer answer){
        answers.add(answer);
    }
}
