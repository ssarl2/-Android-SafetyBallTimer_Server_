package com.ssarl.sbtformanager;

import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.provider.ContactsContract;
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

import java.util.ArrayList;
import java.util.List;

public class GetDataActivity extends AppCompatActivity {
    public DatabaseReference valDatabase;
    List<String> valHouse = new ArrayList<>();
    public int valCount = 0;
    List<String>getQuesNum=new ArrayList<>();
    List<String>getAnswerNum=new ArrayList<>();
    List<String>getTime=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_get_data);
        final String temp;



        // START Get Data from Firebase server
        valDatabase = FirebaseDatabase.getInstance().getReference();


        // START Get all token from Firebase server
        if (valDatabase.child("Answers").getKey() != null) {// 유저 존재여부 확인
            valDatabase.child("Answers").addValueEventListener(new ValueEventListener()
            {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        valHouse.add(snapshot.getValue().toString());
                        String[][] DataArr=null;
                        Log.d("답! : " + valCount, valHouse.get(valCount));
                        String temp = (valHouse.get(valCount)).substring(13);
                        int index= temp.indexOf(",");
                        temp=temp.substring(0,index);
                        String temp2 = (valHouse.get(valCount)).substring((valHouse.get(valCount)).lastIndexOf("=")+1);
                        int index2=temp2.indexOf("}");
                        temp2=temp2.substring(0,index2);
                        String temp3=(valHouse.get(valCount)).substring(valHouse.get(valCount).lastIndexOf("-")+1);
                        int index3=temp3.indexOf(",");
                        temp3=temp3.substring(0,index3);

                        getQuesNum.add(temp);
                        getAnswerNum.add(temp2);
                        getTime.add(temp3);
                        Log.d("답2",getQuesNum.get(valCount));
                        Log.d("답3",getAnswerNum.get(valCount));
                        Log.d("답4",getTime.get(valCount));
                        valCount++;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Intent intent = new Intent(getApplicationContext(), Graph_activity.class);
            startActivity(intent);
        }
    }
}
