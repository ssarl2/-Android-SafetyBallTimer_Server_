package com.ssarl.sbtformanager.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.ssarl.sbtformanager.Object.Answers;
import com.ssarl.sbtformanager.Object.EachValue;
import com.ssarl.sbtformanager.R;

import java.util.ArrayList;
import java.util.Collections;

public class TimeValueListviewAdapter extends BaseAdapter {

    DataPoint[] dataPoint;
    ArrayList<Integer> ids;

    ArrayList<Answers> answersArrayList;


    public TimeValueListviewAdapter(ArrayList<Answers> answersArrayList) {
        this.answersArrayList = answersArrayList;
        ids = new ArrayList<>(answersArrayList.size());
    }

    @Override
    public int getCount() {
        return answersArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
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

        int dataPointSize = answersArrayList.get(position).getEachValue().size(); //you need to match the dataPoint size to match number of values.

        dataPoint = new DataPoint[dataPointSize];

        // you have to sort x-value by ASC before you input the data into DataPoint
        ArrayList<EachValue> eachValueArrayList = myDataPointSort(answersArrayList.get(position));

        for (int i = 0; i < dataPointSize; i++) {

            int xPos = Integer.parseInt(eachValueArrayList.get(i).getSentTime());
            int yPos = eachValueArrayList.get(i).getValue();

            dataPoint[i] = new DataPoint(xPos, yPos);
        }


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
//        gridLabelRenderer.setNumHorizontalLabels(6);
//        gridLabelRenderer.setNumVerticalLabels(3);

//        graph.setTitle("Questions " + (position + 1));
        graph.setTitle(answersArrayList.get(position).getQuestion());
        graph.setTitleTextSize(35);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(24);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setScrollable(true);
        graph.removeAllSeries();
        graph.addSeries(series);

        return convertView;

    }



    public ArrayList<EachValue> myDataPointSort(Answers answers) {

        ArrayList<EachValue> eachValueArrayList = new ArrayList<>();

        eachValueArrayList.addAll(answers.getEachValue());

        Collections.sort(eachValueArrayList);

        return eachValueArrayList;
    }

}
