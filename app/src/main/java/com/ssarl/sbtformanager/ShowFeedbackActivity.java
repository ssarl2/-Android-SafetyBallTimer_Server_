package com.ssarl.sbtformanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class ShowFeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    ListView listView;

    Button deleteBtn;
    Button confirmBtn;

    private ArrayAdapter arrayAdapter;

    private ArrayList<String> feedbackArrayList;

    private HashSet<String> removedItemsSet;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_feedback);


        listView = (ListView) findViewById(R.id.listviewFeedback);

        deleteBtn = findViewById(R.id.deleteFeedback);
        confirmBtn = findViewById(R.id.confirmFeedback);


        Intent intent = getIntent();

        feedbackArrayList = (ArrayList<String>) intent.getSerializableExtra("feedbackArrayList");

        // equip list to adapter and listview shows question contents
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, feedbackArrayList);
        listView.setAdapter(arrayAdapter);


        removedItemsSet = new HashSet<>();


        deleteBtn.setOnClickListener(this);


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.child("Feedback").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // get existing token from firebase 파베에 있는 토큰 값 받기
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // execute for syntax in order to get whole data 데이터 전체를 받기위해 반복문 실행

                            String key = snapshot.getValue().toString();

                            if (removedItemsSet.contains(key)) { // if there are keys which are deleted, remove certainly
                                snapshot.getRef().removeValue(); // remove selected a Feedback
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }



    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            this.finish();
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }



    @Override
    public void onClick (View v){ // remove list

        SparseBooleanArray checkedItems = listView.getCheckedItemPositions(); // input checkitems from listview
        int count = arrayAdapter.getCount();

        for (int i = count - 1; i >= 0; i--) {
            if (checkedItems.get(i)) { // if there are keys chosen from listview, it put values individually to 'copy1, 2' variable

                removedItemsSet.add(feedbackArrayList.get(i));

                feedbackArrayList.remove(i);
            }

        }

        // 모든 선택 상태 초기화.
        listView.clearChoices();

        arrayAdapter.notifyDataSetChanged();
    }


    // If there is no this method, you can't insert data after delete
    @Override
    protected void onPause() {
        feedbackArrayList.clear();
        removedItemsSet.clear();
        arrayAdapter.clear();

        super.onPause();
    }
}