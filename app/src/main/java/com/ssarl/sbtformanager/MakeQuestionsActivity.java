package com.ssarl.sbtformanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssarl.sbtformanager.Object.Answers;

import java.util.ArrayList;


public class MakeQuestionsActivity extends AppCompatActivity {



    ImageButton finishBtn;

    EditText question;

    String insertQ;

    private ArrayList<Answers> answersArrayList;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_questions);


        Intent intent = getIntent();

        answersArrayList = (ArrayList<Answers>) intent.getSerializableExtra("answersArrayList");


        finishBtn = (ImageButton)findViewById(R.id.finishMake);


        finishBtn.setOnClickListener(new View.OnClickListener() { // when touch Finish button Finish 버튼 누를 시

            @Override
            public void onClick(View view) {

                question = (EditText)findViewById(R.id.makeQuestions);
                insertQ = question.getText().toString();

                    databaseReference.child("Analyze").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            int index = (int)dataSnapshot.getChildrenCount(); // declare index variable in order to choose last data 마지막 데이터를 선별하기 위한 index 변수 선언
                            int questionNum = answersArrayList.get(index - 1).getQue_num() + 1;

                            Answers answers = new Answers();

                            answers.setQue_num(questionNum);
                            answers.setCount(0);
                            answers.setTotal_value(0);
                            answers.setQuestion(insertQ);

                            databaseReference.child("Analyze").push().setValue(answers);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                startActivity(intent);
                finish();
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
}
