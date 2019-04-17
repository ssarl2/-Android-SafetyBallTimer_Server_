package com.ssarl.sbtformanager;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class Graph_activity extends AppCompatActivity {

    private ListView listView;
    public DatabaseReference valDatabase;
    int que_num;
    //    private ArrayList<Answer> answers = new ArrayList<>();
//    Answers answers = new Answers();
    static int x = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_activity);

        final ArrayList<Answers> answersArrayList = new ArrayList<>();
        final ArrayList<Answers> tempArrayList = new ArrayList<>();
//        final ArrayList<Analyze> analyzeArray = new ArrayList<>();
        final ArrayList<EachValue> eachValueArrayList = new ArrayList<>();
        final ArrayList<String> id = new ArrayList<>();
        final ImageButton QuestionGraph = (ImageButton) findViewById(R.id.question_graph);
        final ImageButton backToMain = (ImageButton) findViewById(R.id.backToMain);

        // START Get Data from Firebase server
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();

        databaseReference.child("Analyze").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("and next.: ", "something to do........");
                final ArrayList<String> keys = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Answers answers = snapshot.getValue(Answers.class);




//                    Log.e("count: ", answers.count+"");
//                    Log.e("que_num: ", answers.que_num+"");
//                    Log.e("total: ", answers.total_value+"");
//                    Log.i("value: ", answers.eachValue+"");

//                    Log.e("value: ", answers.eachValue.get(0).value+"");
//                    Log.e("time: ", answers.eachValue.get(0).sentTime);

//                    answersArrayList.add(answers);


//                    id.add(snapshot.getKey());
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

                                Log.i( "String TokenNi: ",eachValue.sentTime);
                                eachValue.sentTime = st.nextToken();
                                eachValue.value = eachValueData.value;

                                eachValueArrayList.add(eachValueData);

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
//                                Log.i("eachValue-repeatTime", answersArrayList.get(0).eachValue.get(0).sentTime);

                            eachValueArrayList.clear(); //the eachValueArrayList.clear() command is clearing the eachValue from inside the answersArrayList also

//                                for (int i = 0; i < answersArrayList.size(); i++) {
//                                    Log.i("eachValue-repeatTime", answersArrayList.get(x).eachValue.get(i).sentTime);
//                                    Log.i("eachValue-repeatTime", answersArrayList.get(x).eachValue.get(i).value+"");
//                                }
                            x++;
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                    // END Get inner data
                }


//                    Log.e("count: ", answersArrayList.get(0).count+"");
//                    Log.e("que_num: ", answersArrayList.get(0).que_num+"");
//                    Log.e("total: ", answersArrayList.get(0).total_value+"");
//                    Log.e("value: ", answersArrayList.get(0).eachValue.get(0).value+"");
//                    Log.e("time: ", answersArrayList.get(0).eachValue.get(0).sentTime);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        GraphView graph = (GraphView) findViewById(R.id.graph);

        DataPoint[] dataPoints = new DataPoint[answersArrayList.size()];
        //Collections.sort(answers);

        Log.d("안되나", Integer.toString(answersArrayList.size()));
        for (int i = 0; i < answersArrayList.size(); i++) {
            int xPos = answersArrayList.get(i).que_num;
            int yPos = 0;
            if (answersArrayList.get(i).count > 0) {
                yPos = answersArrayList.get(i).total_value / answersArrayList.get(i).count; // 한 문제의 총 답변 값 / 답변 수
            }
            dataPoints[i] = new DataPoint(xPos, yPos);
            Log.d("안되나", Double.toString(xPos));
        }
        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPoints);
        series.resetData(dataPoints);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(answersArrayList.size());
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.setTitle("Answer average");
        series.setColor(Color.BLUE);
        graph.addSeries(series);

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
}
