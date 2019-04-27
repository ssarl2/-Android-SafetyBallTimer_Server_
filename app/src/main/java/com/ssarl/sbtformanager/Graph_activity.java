package com.ssarl.sbtformanager;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class Graph_activity extends AppCompatActivity {

    private GraphView graph;
    private DataPoint[] dataPoints;
    private BarGraphSeries<DataPoint> series;
    private ArrayList<Answers> answersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_activity);

        answersArrayList = new ArrayList<>();
        final ArrayList<Answers> tempArrayList = new ArrayList<>();
        final ArrayList<EachValue> eachValueArrayList = new ArrayList<>();
        final ImageButton QuestionGraph = (ImageButton) findViewById(R.id.question_graph);
        final ImageButton backToMain = (ImageButton) findViewById(R.id.backToMain);

        // START Get Data from Firebase server
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Analyze").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("and next.: ", "something to do........");
                final ArrayList<String> keys = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Answers answers = snapshot.getValue(Answers.class);

                    Log.i("analyze.count", answers.count + "");
                    if (answers.count > 0) {
                        keys.add(snapshot.getKey());
                        tempArrayList.add(answers);

                    } else {
                        Log.i("onDataChange: ", "Else, Access");
                        keys.add("empty");
                        tempArrayList.add(answers);
                        //answersArrayList.add(answers);

                    }


                }
                // START Get inner data

                for(int i = 0; i < keys.size(); i++) {

                    final int count = i;
                    //answers with EachValue are looped

                    databaseReference.child("Analyze").child(keys.get(i)).child("EachValue").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.e("Enter", "---------");
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                EachValue eachValueData = snapshot.getValue(EachValue.class);

                                EachValue eachValue = new EachValue();

                                StringTokenizer st = new StringTokenizer(eachValueData.sentTime,":");

                                eachValue.sentTime = st.nextToken();
                                eachValue.value = eachValueData.value;
                                Log.i( "String TokenNi: ",eachValue.sentTime);

                                eachValueArrayList.add(eachValue);

                            }

                            Collections.sort(eachValueArrayList);
//                                Answers answersBox = new Answers();
                            //tempArrayList.get(count).eachValue.clear();
                            //tempArrayList.get(count).eachValue = eachValueArrayList;  // I can't input data to here

                            for(EachValue eachValue : eachValueArrayList) {
                                tempArrayList.get(count).eachValue.add(eachValue);

                            }
                            answersArrayList.add(tempArrayList.get(count));

                            for (int i = 0; i < eachValueArrayList.size(); i++) {
                                Log.i("eachValue-repeatTime", eachValueArrayList.get(i).sentTime);
                                Log.i("eachValue-repeatTime", eachValueArrayList.get(i).value+"");
                            }
                            //answersArrayList.add(tempArrayList.get(j));

                            eachValueArrayList.clear(); //the eachValueArrayList.clear() command is clearing the eachValue from inside the answersArrayList also

                            if(answersArrayList.size() == tempArrayList.size()) updateGraph();
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                    // END Get inner data
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        graph = (GraphView) findViewById(R.id.graph);
        GridLabelRenderer gridLabelRenderer = graph.getGridLabelRenderer();

        gridLabelRenderer.setHorizontalAxisTitle("Question Number");
        gridLabelRenderer.setVerticalAxisTitle("Value");
//        gridLabelRenderer.setNumHorizontalLabels(100);
//        gridLabelRenderer.setNumVerticalLabels(4);

        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setScrollable(true);
        graph.setTitle("Answer average");

        //dataPoints = new DataPoint[answersArrayList.size()];




        QuestionGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(), Graph_activity_chooseQuestion.class);
                Log.i("graph.activy.size", String.valueOf(answersArrayList.get(0).eachValue.size()));

                intent.putExtra("answersArray", answersArrayList);

                startActivity(intent);
                finish();
            }
        });

        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public void updateGraph() {
        dataPoints = new DataPoint[answersArrayList.size()];
        for (int i = 0; i < answersArrayList.size(); i++) {
            int xPos = answersArrayList.get(i).que_num;
            int yPos = 0;
            if (answersArrayList.get(i).count > 0) {
                yPos = answersArrayList.get(i).total_value / answersArrayList.get(i).count; // total answers of a question / answers 한 문제의 총 답변 값 / 답변 수
            }
            dataPoints[i] = new DataPoint(xPos, yPos);
        }
        series = new BarGraphSeries<>(dataPoints);
        series.resetData(dataPoints);
        series.setColor(Color.BLUE);
        series.setDataWidth(0.4);
//        graph.getViewport().setMaxX(10);
        graph.addSeries(series);
    }
}
