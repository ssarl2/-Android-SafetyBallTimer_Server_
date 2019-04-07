package com.ssarl.sbtformanager;

import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Graph_activity extends AppCompatActivity {

    private ListView listView;
    public DatabaseReference valDatabase;
    Spinner spinner;
    int que_num;
    private ArrayList<Answer> answers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_activity);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference MyRef = mDatabase.getReference();

        final List<Integer> data = new ArrayList<>();
        final ArrayList<Analyze> analyzes = new ArrayList<>();
        final ArrayList<EachValue> value = new ArrayList<EachValue>();
        spinner = (Spinner)findViewById(R.id.spinner);
        final com.ssarl.sbtformanager.SpinnerAdapter spinnerAdapter = new com.ssarl.sbtformanager.SpinnerAdapter(this, data);
        final Button QuestionGraph = (Button)findViewById(R.id.question_graph);
        final Button backToMain = (Button)findViewById(R.id.backToMain);

        spinner.setAdapter(spinnerAdapter);

        // START Get Data from Firebase server
        valDatabase = FirebaseDatabase.getInstance().getReference();

        valDatabase.child("Analyze").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Analyze analyze = snapshot.getValue(Analyze.class);
                    data.add(analyze.que_num);
                    analyzes.add(analyze);
                }

                GraphView graph = (GraphView) findViewById(R.id.graph);


                DataPoint[] dataPoints = new DataPoint[analyzes.size()];
                //Collections.sort(answers);

                Log.d("안되나",Integer.toString(analyzes.size()));
                for(int i=0; i<analyzes.size(); i++){
                    int xPos = analyzes.get(i).que_num;
                    int yPos = 0;
                    if(analyzes.get(i).count > 0) {
                        yPos = analyzes.get(i).total_value / analyzes.get(i).count; // 한 문제의 총 답변 값 / 답변 수
                    }
                    dataPoints[i] = new DataPoint(xPos, yPos);
                    Log.d("안되나",Double.toString(xPos));
                }
                BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(dataPoints);
                series.resetData(dataPoints);
                graph.getViewport().setMinX(0);
                graph.getViewport().setMaxX(analyzes.size());
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(100);
                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setYAxisBoundsManual(true);
                series.setColor(Color.BLUE);
                graph.addSeries(series);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        QuestionGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(), Graph_activity_chooseQuestion.class);
                que_num = spinnerAdapter.getCurrentQuestionNumber();
                intent.putExtra("que_num", que_num);
                intent.putExtra("aalyze_data", (ArrayList<Analyze>)analyzes);

                valDatabase.child("Analyze").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Analyze analyze = snapshot.getValue(Analyze.class);
                            String id = snapshot.getKey();
                            if(analyze.que_num == que_num){
                                valDatabase.child("Analyze").child(id).child("EachValue").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                EachValue data = snapshot.getValue(EachValue.class);
                                                value.add(data);

                                            }
                                            Log.d("들아옴?", "?????");
                                            intent.putExtra("values", (ArrayList<EachValue>)value);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "At Current, This question doesn't have answer so that can't show graph from analyzed that", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });
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
