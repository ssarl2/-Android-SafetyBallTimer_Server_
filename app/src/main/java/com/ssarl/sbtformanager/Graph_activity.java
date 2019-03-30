package com.ssarl.sbtformanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Graph_activity extends AppCompatActivity {
    private ListView listView;
    private List<Question> list;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_activity);
        listView = (ListView) findViewById(R.id.listview);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference MyRef = mDatabase.getReference();
        Log.d("데이터베이스 작동 중", "ddddddddd");

        list = new ArrayList<Question>();
        final ListviewActivity listviewAdapter = new ListviewActivity(this, R.layout.custom_list, list);

        MyRef.child("Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Question data = snapshot.getValue(Question.class);
                    list.add(data);
                    count++;
                    Log.d("size", data.question_num);
                }
                listviewAdapter.notifyDataSetChanged(); // 리스트뷰 갱신 ( 이 코드가 있어야 데이터베이스를 기준으로 둔 리스트뷰를 볼 수 있음. )
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        listView.setAdapter(listviewAdapter);
    }
}