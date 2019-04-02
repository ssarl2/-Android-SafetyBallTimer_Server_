package com.ssarl.sbtformanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;

/**
 * Created by Wonhak on 2019-04-02.
 */

public class Graph_activity_chooseQuestion extends AppCompatActivity {
    DataPoint[] dataPoints;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_activity_choosequestion);

        Intent intent = getIntent();
        int que_num = intent.getExtras().getInt("que_num");
        ArrayList<Analyze> analyzes = new ArrayList<Analyze>();
        analyzes = (ArrayList<Analyze>)intent.getSerializableExtra("aalyze_data");
        ArrayList<EachValue> values = new ArrayList<EachValue>();
        values = (ArrayList<EachValue>)intent.getSerializableExtra("values");
        Button backbtn = findViewById(R.id.backbtn);

        Log.d("문제 번호", Integer.toString(que_num));
        Log.d("사이즈", Integer.toString(analyzes.size()));
        Log.d("시간 사이즈", Integer.toString(values.size()));

        GraphView graph = (GraphView) findViewById(R.id.graph);
        dataPoints = new DataPoint[values.size()];
        for(int i=0; i<values.size(); i++){
            int xPos = Integer.parseInt(values.get(i).senttime);
            int yPos = values.get(i).value;
            dataPoints[i] = new DataPoint(xPos, yPos);
        }
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(dataPoints);
        graph.addSeries(series);
        graph.setTitle("Question "+Integer.toString(que_num));
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(24);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        series.setShape(PointsGraphSeries.Shape.POINT);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Graph_activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
