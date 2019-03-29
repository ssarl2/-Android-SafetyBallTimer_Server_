package com.ssarl.sbtformanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Graph_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_activity);
        int num=1;
        GraphView graph1 = (GraphView)findViewById(R.id.graph1);



        LineGraphSeries<DataPoint> series=new LineGraphSeries<>(getDataPoint());
        graph1.addSeries(series);

    }

    private DataPoint[] getDataPoint() {
        DataPoint[] answer = new DataPoint[]{
                new DataPoint(1000/60,1),//x에 now 값 Y에 Q값에 따른 answer값
                new DataPoint(1065/60,6),
                new DataPoint(1550/60,5),


        };

        return (answer);
    }
}