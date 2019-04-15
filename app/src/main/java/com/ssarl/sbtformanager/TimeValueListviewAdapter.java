package com.ssarl.sbtformanager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

public class TimeValueListviewAdapter extends BaseAdapter {

    DataPoint[] dataPoint;
    ArrayList<Answers> answersArray;

    public TimeValueListviewAdapter(ArrayList<Answers> answersArray) {
        this.answersArray = answersArray;
    }
    @Override
    public int getCount() {
        Log.e( "getCountInAdapter: ", String.valueOf(answersArray.size()));
        return answersArray.size();
//        return 1;
    }

    @Override
    public Object getItem(int i) {
        Log.e( "getItem: ", String.valueOf(i));
        return i;
    }

    @Override
    public long getItemId(int i) {
        Log.e( "getItemID: ", String.valueOf(i));
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_listview, viewGroup, false);
        }

//        Random rnd = new Random();

        dataPoint = new DataPoint[24];
//        for (int i = 0; i < 24; i++) {
//            int xPos = i;
//            int yPos = rnd.nextInt(100) + 0;
//            dataPoint[i] = new DataPoint(xPos, yPos);
//        }

        Log.i("Position: ", String.valueOf(position));

            for (int i = 0; i < answersArray.get(position).eachValue.size(); i++) {
                Log.i("size: ", String.valueOf(answersArray.get(position).eachValue.size()));
                String str = answersArray.get(position).eachValue.get(i).sentTime;
                StringTokenizer st = new StringTokenizer(str,":-");

                int xPos = Integer.parseInt(st.nextToken());
                Log.i("TimeValue: ", String.valueOf(xPos));
                int yPos = answersArray.get(position).eachValue.get(i).value;
                dataPoint[position] = new DataPoint(xPos, yPos);
            }

            if(dataPoint == null){
                dataPoint[position] = new DataPoint(0,0);
            }

        GraphView graph = (GraphView) view.findViewById(R.id.graph);
        GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();
        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(dataPoint);

        series.setColor(Color.BLUE);
        series.setShape(PointsGraphSeries.Shape.POINT);
        series.setSize(6);

        gridLabelRenderer.setHorizontalAxisTitle("Time");
        gridLabelRenderer.setVerticalAxisTitle("Value");
        gridLabelRenderer.setNumHorizontalLabels(4);
        gridLabelRenderer.setNumVerticalLabels(3);

        graph.setTitle("Question "+position);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(12);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setScrollable(true);
        graph.addSeries(series);

        return view;
    }

}
