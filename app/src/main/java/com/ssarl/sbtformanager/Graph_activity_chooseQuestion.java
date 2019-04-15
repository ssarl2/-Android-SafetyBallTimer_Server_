package com.ssarl.sbtformanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Wonhak on 2019-04-02.
 */

public class Graph_activity_chooseQuestion extends AppCompatActivity {
//    DataPoint[] dataPoints;

    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_activity_choosequestion);

        Intent intent = getIntent();
        ArrayList<Answers> answersArray = (ArrayList<Answers>) intent.getSerializableExtra("answersArray");

//        Log.e("count: ", answersArray.get(0).count+"");
//        Log.e("que_num: ", answersArray.get(0).que_num+"");
//        Log.e("total: ", answersArray.get(0).total_value+"");
//        Log.e("value: ", answersArray.get(0).eachValue.get(0).value+"");
//        Log.e("time: ", answersArray.get(0).eachValue.get(0).sentTime);


        Log.i("graph.size", String.valueOf(answersArray.get(0).eachValue.size()));
        Button backbtn = findViewById(R.id.backbtn);
        listView = (ListView)findViewById(R.id.listview);
        TimeValueListviewAdapter timeValueListviewAdapter = new TimeValueListviewAdapter(answersArray);
        listView.setAdapter(timeValueListviewAdapter);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Graph_activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
