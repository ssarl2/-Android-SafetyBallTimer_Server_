package com.ssarl.sbtformanager;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssarl.sbtformanager.Object.Answers;
import com.ssarl.sbtformanager.Object.EachValue;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    Button sendNotificationsBtn;
    Button makeQuestionsBtn;
    Button averageGraphBtn;
    Button deleteQuestionsBtn;
    Button feedbackBtn;
    Button deleteUsersBtn;


    private ArrayList<Answers> answersArrayList = new ArrayList<>();

    private ArrayList<EachValue> eachValueArrayList = new ArrayList<>();

    private ArrayList<String> feedbackArrayList = new ArrayList<>();
    private ArrayList<String> tokenArrayList = new ArrayList<>();

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendNotificationsBtn = (Button) findViewById(R.id.sendNotificationsBtn);
        makeQuestionsBtn = (Button) findViewById(R.id.makeQuestionsBtn);
        averageGraphBtn = (Button) findViewById(R.id.averageGraphBtn);
        deleteQuestionsBtn = (Button) findViewById(R.id.deleteQuestionsBtn);
        feedbackBtn = (Button) findViewById(R.id.feedbackBtn);
        deleteUsersBtn = (Button) findViewById(R.id.deleteUsersBtn);

        sendNotificationsBtn.setEnabled(false);
        makeQuestionsBtn.setEnabled(false);
        averageGraphBtn.setEnabled(false);
        deleteQuestionsBtn.setEnabled(false);
        feedbackBtn.setEnabled(false);
        deleteUsersBtn.setEnabled(false);


        answersArrayList.clear();
        eachValueArrayList.clear();
        feedbackArrayList.clear();
        tokenArrayList.clear();





        Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                sendNotificationsBtn.setEnabled(true);
                makeQuestionsBtn.setEnabled(true);
                averageGraphBtn.setEnabled(true);
                deleteQuestionsBtn.setEnabled(true);
                feedbackBtn.setEnabled(true);
                deleteUsersBtn.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Complete to get Data", Toast.LENGTH_SHORT).show();
            }
        }.start();







        // START Get Data from Firebase server

        // START Get all token from Firebase server
        if (databaseReference.child("getToken").getKey() != null) { // check whether users exist or not 유저 존재여부 확인
            databaseReference.child("getToken").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // get existing token from firebase 파베에 있는 토큰 값 받기
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // execute for syntax in order to get whole data 데이터 전체를 받기위해 반복문 실행
                        tokenArrayList.add(snapshot.getValue().toString());// put token into token ArrayList 토큰배열에 넣기
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else { // if there is nothing, show toast message 존재하지 않으면 Toast 메시지 띄움
            sendNotificationsBtn.setEnabled(false);
            Toast.makeText(getApplicationContext(), "In current, there is no user in database.", Toast.LENGTH_LONG).show();
        }
        // END Get all token from Firebase server







        // START Get Data from Firebase server
        databaseReference.child("Analyze").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> keys = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Answers answers = snapshot.getValue(Answers.class);
                    keys.add(snapshot.getKey());
                    answersArrayList.add(answers);
                }
                // START Get inner data

                for (int i = 0; i < keys.size(); i++) {

                    //answers with EachValue are looped

                    final int finalInt = i;
                    databaseReference.child("Analyze").child(keys.get(i)).child("EachValue").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int childern = (int) dataSnapshot.getChildrenCount();
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                EachValue eachValue = snapshot.getValue(EachValue.class);

                                StringTokenizer st = new StringTokenizer(eachValue.getSentTime(), ":");

                                eachValue.setSentTime(st.nextToken());

                                eachValueArrayList.add(eachValue);

                                if (childern == 1) {
                                    Answers answers = new Answers();

                                    answers.setEachValue(eachValueArrayList);

                                    answers.setQuestion(answersArrayList.get(finalInt).getQuestion());
                                    answers.setCount(answersArrayList.get(finalInt).getCount());
                                    answers.setQue_num(answersArrayList.get(finalInt).getQue_num());
                                    answers.setTotal_value(answersArrayList.get(finalInt).getTotal_value());

                                    answersArrayList.set(finalInt, answers);
                                }
                                childern--;
                            }
                            eachValueArrayList.clear();
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







        // get feedback
        databaseReference.child("Feedback").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // get existing token from firebase 파베에 있는 토큰 값 받기
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // execute for syntax in order to get whole data 데이터 전체를 받기위해 반복문 실행
                    String feedback = snapshot.getValue().toString();
                    feedbackArrayList.add(feedback);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        sendNotificationsBtn.setOnClickListener(this);
        makeQuestionsBtn.setOnClickListener(this);
        averageGraphBtn.setOnClickListener(this);
        deleteQuestionsBtn.setOnClickListener(this);
        feedbackBtn.setOnClickListener(this);
        deleteUsersBtn.setOnClickListener(this);


    } // End onCreate

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.sendNotificationsBtn:

                intent = new Intent(getApplicationContext(), SendNotificationsActivity.class);
                intent.putExtra("answersArrayList", answersArrayList);
                intent.putExtra("tokenArrayList", tokenArrayList);

                break;
            case R.id.makeQuestionsBtn:

                intent = new Intent(getApplicationContext(), MakeQuestionsActivity.class);
                intent.putExtra("answersArrayList", answersArrayList);

                break;
            case R.id.averageGraphBtn:

                intent = new Intent(getApplicationContext(), GraphAverageActivity.class);
                intent.putExtra("answersArrayList", answersArrayList);

                break;
            case R.id.deleteQuestionsBtn:

                intent = new Intent(getApplicationContext(), DeleteQuestionsActivity.class);
                intent.putExtra("answersArrayList", answersArrayList);

                break;
            case R.id.feedbackBtn:

                intent = new Intent(getApplicationContext(), ShowFeedbackActivity.class);
                intent.putExtra("feedbackArrayList", feedbackArrayList);

                break;
            case R.id.deleteUsersBtn:

                intent = new Intent(getApplicationContext(), DeleteUsersActivity.class);
                intent.putExtra("tokenArrayList", tokenArrayList);

                break;
        }
        startActivity(intent);
        finish();
    }


}