package com.ssarl.sbtformanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Graph_activity extends AppCompatActivity {
    private ListView listView;
    final ListviewActivity listviewAdapter = new ListviewActivity();
    int count = 0;
    FirebaseDatabase mDatabase;
    DatabaseReference MyRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_activity);
        listView = (ListView) findViewById(R.id.listview);

        mDatabase = FirebaseDatabase.getInstance();
        MyRef = mDatabase.getReference();

        final ArrayList<Question> arrayList = new ArrayList<>();


        MyRef.child("Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("데이터베이스 작동 중", "ddddddddd");
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Question data = snapshot.getValue(Question.class);
                    arrayList.add(data);
                    count++;
                    Log.d("size", Integer.toString(count));
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
        listView.setAdapter(listviewAdapter);
    }
}