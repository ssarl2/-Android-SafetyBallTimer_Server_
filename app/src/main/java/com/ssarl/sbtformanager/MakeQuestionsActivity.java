package com.ssarl.sbtformanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MakeQuestionsActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_questions);

        ImageButton finish_btn = (ImageButton)findViewById(R.id.finish);
        ImageButton cancel_btn = (ImageButton)findViewById(R.id.cancel);
        final EditText edit_question = (EditText)findViewById(R.id.question);

        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = mDatabase.getReference();

        finish_btn.setOnClickListener(new View.OnClickListener() { // when touch Finish button Finish 버튼 누를 시
            @Override
            public void onClick(View view) {

                    mRef.child("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                int index = 0; // declare index variable in order to choose last data 마지막 데이터를 선별하기 위한 index 변수 선언
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    index++;
                                }
                                Question question = new Question(); // set Question class   Question 클래스 초기화
                                question.question_num = String.valueOf(index);  // store index into question_num   question_num 에 index 저장
                                question.question_content = edit_question.getText().toString();
                                mRef.child("Questions").push().setValue(question); // put data 데이터 삽입
                                Answers answers = new Answers();
                                answers.que_num = index;
                                answers.count = 0;
                                mRef.child("Analyze").push().setValue(answers);
                                finish();
                            }
                            else{
                                Question question = new Question();
                                question.question_num = "0";
                                question.question_content = edit_question.getText().toString();
                                mRef.child("Questions").push().setValue(question);
                                Answers answers = new Answers();
                                answers.que_num = 0;
                                answers.count = 0;
                                mRef.child("Analyze").push().setValue(answers);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() { // Cancel 버튼 누를 시
            @Override
            public void onClick(View view) {
                new Intent(getApplicationContext(), MainActivity.class);
                finish();
            }
        });

    }
}
