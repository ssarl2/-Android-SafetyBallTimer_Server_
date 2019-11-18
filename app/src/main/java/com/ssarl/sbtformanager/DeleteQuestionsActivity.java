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
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ssarl.sbtformanager.Object.Answers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class DeleteQuestionsActivity extends AppCompatActivity implements View.OnClickListener{



    ListView listView;

    Button deleteBtn;
    Button confirmBtn;

    private ArrayAdapter arrayAdapter;

    private HashMap<String, Integer> questionMap;

    private HashSet<Integer> removedItemsSet;

    private ArrayList<String> listViewArrayList;
    private ArrayList<Answers> answersArrayList; // create List variable in order to put Question class Question 클래스를 담을 List 변수 생성

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_questions);


        Intent intent = getIntent();

        answersArrayList = (ArrayList<Answers>) intent.getSerializableExtra("answersArrayList");

        deleteBtn = findViewById(R.id.deleteQuestions);
        confirmBtn = findViewById(R.id.confirmQuestions);

        listView = (ListView) findViewById(R.id.listviewQuestions);

        listViewArrayList = new ArrayList<>();
        questionMap = new HashMap<>();
        removedItemsSet = new HashSet<>();


        // put only question_content to arrayList from intent values to show list
        // consist of questionMap from answersList to remove data in database
        for (Answers answers: answersArrayList) {
            listViewArrayList.add(answers.getQuestion());

            String string = answers.getQuestion();
            int integer = answers.getQue_num();

            questionMap.put(string, integer);
        }

        // equip list to adapter and listview shows question contents
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, listViewArrayList);
        listView.setAdapter(arrayAdapter);



        deleteBtn.setOnClickListener(this);



        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // process certainly to remove key from Analyze


                databaseReference.child("Analyze").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // get existing token from firebase 파베에 있는 토큰 값 받기
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { // execute for syntax in order to get whole data 데이터 전체를 받기위해 반복문 실행
                            Answers answers = snapshot.getValue(Answers.class);

                            int i = answers.getQue_num();

                            if(removedItemsSet.contains(i)){ // if there are keys which are deleted, remove certainly
                                snapshot.getRef().removeValue(); // remove selected a Analyze
                                Toast.makeText(getApplicationContext(), "Complete", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
    public void onClick(View v) {

                SparseBooleanArray checkedItems = listView.getCheckedItemPositions(); // input checkitems from listview
                int count = arrayAdapter.getCount();

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) { // if there are keys chosen from listview, it put values individually to 'copy1, 2' variable

                        int integer = questionMap.get(listViewArrayList.get(i));

                        removedItemsSet.add(integer);

                        listViewArrayList.remove(i);
                    }
                }

                // 모든 선택 상태 초기화.
                listView.clearChoices() ;

                arrayAdapter.notifyDataSetChanged();


        }


    // If there is no this method, you can't insert data after delete
    @Override
    protected void onPause() {
        removedItemsSet.clear();
        arrayAdapter.clear();

        super.onPause();
    }

}