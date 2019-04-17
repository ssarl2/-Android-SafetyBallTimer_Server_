package com.ssarl.sbtformanager;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.StringTokenizer;

public class TimeValueListviewAdapter extends BaseAdapter {

    DataPoint[] dataPoint;
    ArrayList<Answers> answersArray;
    ArrayList<Integer> ids;

    public TimeValueListviewAdapter(ArrayList<Answers> answersArray) {
        this.answersArray = answersArray;
        ids = new ArrayList<>(answersArray.size());
    }

    @Override
    public int getCount() {
        Log.e("getCountInAdapter: ", String.valueOf(answersArray.size()));
        return answersArray.size();
//        return 1;
    }

    @Override
    public Object getItem(int i) {
        Log.e("getItem: ", String.valueOf(i));
        return i;
    }

    @Override
    public long getItemId(int i) {
        Log.e("getItemID: ", String.valueOf(i));
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        if (convertView == null) {

            ids.add(position, ViewCompat.generateViewId());
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview, parent, false);

        }

        // ...


    /*
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_listview, viewGroup, false);
        }
*/
//        Random rnd = new Random();

        int pointArraySize = answersArray.get(position).eachValue.size(); //you need to match the dataPoint size to match number of values.
        dataPoint = new DataPoint[pointArraySize];
//        for (int i = 0; i < 24; i++) {
//            int xPos = i;
//            int yPos = rnd.nextInt(100) + 0;
//            dataPoint[i] = new DataPoint(xPos, yPos);
//        }

//        ArrayList<Integer> xPos = new ArrayList<>();
//        ArrayList<Integer> yPos = new ArrayList<>();

        Log.i("Position: ", String.valueOf(position));
        Log.i("size: ", String.valueOf(answersArray.get(position).eachValue.size()));
        for (int i = 0; i < answersArray.get(position).eachValue.size(); i++) {


            //using the hour as Xpos value in the PointsGraphSeries won't work because the answers are from different days so order is wrong.
//            String str = answersArray.get(position).eachValue.get(i).sentTime;
//            StringTokenizer st = new StringTokenizer(str,":-");

            //sort the hours in correct order


//            tempxPos.add(Integer.parseInt(st.nextToken()));
//            tempyPos.add(answersArray.get(position).eachValue.get(i).value);
//            Log.i("TimeValue: ", String.valueOf(xPos));

            int xPos = Integer.parseInt(answersArray.get(position).eachValue.get(i).sentTime);
            int yPos = answersArray.get(position).eachValue.get(i).value;
            dataPoint[i] = new DataPoint(xPos, yPos);

        }

//        Collections.sort(tempxPos);

//        for (int i = 0; i < tempxPos.size(); i++) {
//            int xPos = tempxPos.get(i);
//            int yPos = answersArray.get(position).eachValue.get(i).value;
//            dataPoint[i] = new DataPoint(xPos, yPos);
//        }

        if (dataPoint == null) {
            dataPoint[position] = new DataPoint(0, 0);
        }


        GraphView graph = (GraphView) convertView.findViewById(R.id.graph);
        GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();

        PointsGraphSeries<DataPoint> series = new PointsGraphSeries<>(dataPoint);
        series.setColor(Color.BLUE);
        series.setShape(PointsGraphSeries.Shape.POINT);
        series.setSize(6);

        gridLabelRenderer.setHorizontalAxisTitle("Time");
        gridLabelRenderer.setVerticalAxisTitle("Value");
        gridLabelRenderer.setNumHorizontalLabels(4);
        gridLabelRenderer.setNumVerticalLabels(3);

        graph.setTitle("Question " + (position + 1));
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(24);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setScrollable(true);
        graph.removeAllSeries();
        graph.addSeries(series);

        //return view;
        return convertView;

    }

}
