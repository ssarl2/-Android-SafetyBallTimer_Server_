package com.ssarl.sbtformanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ssarl.sbtformanager.Adapter.AverageGraphListviewAdapter;
import com.ssarl.sbtformanager.Object.AverageGraphitem;
import com.ssarl.sbtformanager.Object.Answers;

import java.util.ArrayList;

public class GraphAverageActivity extends AppCompatActivity {


    Button QuestionGraph;

    private ListView listview;

    private ArrayList<Answers> answersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_average);

        QuestionGraph = (Button) findViewById(R.id.question_graph);

        Intent intent = getIntent();

        answersArrayList = (ArrayList<Answers>) intent.getSerializableExtra("answersArrayList");

        ArrayList<AverageGraphitem> averageGraphitems = new ArrayList<>();
        for (int i = 0; i < answersArrayList.size(); i++) {

            String title = answersArrayList.get(i).getQuestion();
            int average = 0;

            if(answersArrayList.get(i).getTotal_value() != 0)
                average = answersArrayList.get(i).getTotal_value() / answersArrayList.get(i).getCount();

            AverageGraphitem item = new AverageGraphitem(title, average);

            averageGraphitems.add(item);
        }

        listview = (ListView)findViewById(R.id.listviewAverageGraph);
        AverageGraphListviewAdapter averageGraphListviewAdapter = new AverageGraphListviewAdapter(averageGraphitems);
        listview.setAdapter(averageGraphListviewAdapter);


        QuestionGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(), GraphEachValueActivity.class);

                intent.putExtra("answersArrayList", answersArrayList);

                startActivity(intent);
                finish();
            }
        });


    } // END onCreate



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
