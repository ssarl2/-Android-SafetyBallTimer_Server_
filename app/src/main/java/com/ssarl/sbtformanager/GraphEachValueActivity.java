package com.ssarl.sbtformanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.ListView;

import com.ssarl.sbtformanager.Adapter.TimeValueListviewAdapter;
import com.ssarl.sbtformanager.Object.Answers;

import java.util.ArrayList;

/**
 * Created by Wonhak on 2019-04-02.
 */

public class GraphEachValueActivity extends AppCompatActivity {

    private ListView listView;

    private ArrayList<Answers> answersArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_each_value);

        Intent intent = getIntent();

        answersArrayList = (ArrayList<Answers>) intent.getSerializableExtra("answersArrayList");

        listView = (ListView)findViewById(R.id.listviewEachValueGraph);

        TimeValueListviewAdapter timeValueListviewAdapter = new TimeValueListviewAdapter(answersArrayList);
        listView.setAdapter(timeValueListviewAdapter);


    }




    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(getApplicationContext(), GraphAverageActivity.class);

            intent.putExtra("answersArrayList", answersArrayList);

            startActivity(intent);

            this.finish();
            return false;
        }
        return super.onKeyUp(keyCode, event);
    }



}
