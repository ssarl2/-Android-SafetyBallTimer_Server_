package com.ssarl.sbtformanager;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
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
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;

public class TimeValueListviewAdapter extends BaseAdapter {

    DataPoint[] dataPoint;
    ArrayList<Answers> answersArray;
    ArrayList<Integer> ids;
    ArrayList<String> questions = new ArrayList<>();

    public TimeValueListviewAdapter(ArrayList<Answers> answersArray) {
        this.answersArray = answersArray;
        ids = new ArrayList<>(answersArray.size());
        getData();
    }

    @Override
    public int getCount() {
        Log.e("getCountInAdapter: ", String.valueOf(answersArray.size()));
        return answersArray.size();
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

        int pointArraySize = answersArray.get(position).eachValue.size(); //you need to match the dataPoint size to match number of values.
        dataPoint = new DataPoint[pointArraySize];

        Log.i("Position: ", String.valueOf(position));
        Log.i("size: ", String.valueOf(answersArray.get(position).eachValue.size()));
        for (int i = 0; i < answersArray.get(position).eachValue.size(); i++) {

            int xPos = Integer.parseInt(answersArray.get(position).eachValue.get(i).sentTime);
            int yPos = answersArray.get(position).eachValue.get(i).value;
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
        gridLabelRenderer.setNumHorizontalLabels(6);
        gridLabelRenderer.setNumVerticalLabels(3);

//        graph.setTitle("Questions " + (position + 1));
        graph.setTitle(questions.get(position));
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

    public void getData() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    questions.add(snapshot.child("question_content").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
