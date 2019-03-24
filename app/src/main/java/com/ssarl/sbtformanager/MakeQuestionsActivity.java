package com.ssarl.sbtformanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

        Button finish_btn = (Button)findViewById(R.id.finish);
        Button cancel_btn = (Button)findViewById(R.id.cancel);
        final EditText edit_question = (EditText)findViewById(R.id.question);

        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference mRef = mDatabase.getReference();

        finish_btn.setOnClickListener(new View.OnClickListener() { // Finish 버튼 누를 시
            @Override
            public void onClick(View view) {
                if(mRef.child("Question").getKey() != null){ // Question 키가 존재할 시
                    mRef.child("Question").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int index = 0; // 마지막 데이터를 선별하기 위한 index 변수 선언
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                index++;
                            }
                            Question question = new Question(); // Question 클래스 초기화
                            question.question_num = Integer.toString(index);  // question_num 에 index 저장
                            question.question_content = edit_question.getText().toString();
                            mRef.child("Question").push().setValue(question); // 데이터 삽입
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Question question = new Question();
                    question.question_num = "0";
                    question.question_content = edit_question.getText().toString();
                    mRef.child("Question").push().setValue(question);
                }
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
