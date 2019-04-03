package com.ssarl.sbtformanager;

        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.jjoe64.graphview.GraphView;
        import com.jjoe64.graphview.series.DataPoint;
        import com.jjoe64.graphview.series.DataPointInterface;
        import com.jjoe64.graphview.series.LineGraphSeries;

        import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    public DataPoint[] dataPoints = null;
    private ArrayList<Answer> answers = new ArrayList<>();
    private ArrayList<Question> questions = new ArrayList<>();
    public ListView mListView;
    LineGraphSeries series;
    int sumArr[] = new int[20];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mListView=(ListView)findViewById(R.id.averagelist) ;
        ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.activity_test);
        mListView.setAdapter(adapter);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mDatabase.getReference();

        final GraphView graph1 = (GraphView)findViewById(R.id.graph1);


        Log.d("파베 전", "11111");

        mRef.child("Answers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Answer answer = snapshot.getValue(Answer.class);
                    answers.add(answer);
                    Log.d("파베 엔설즈", "11111");
                }

                LineGraphSeries<DataPoint> series=new LineGraphSeries<>(getDataPoint());
                graph1.addSeries(series);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRef.child("Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Question question = snapshot.getValue(Question.class);
                    questions.add(question);
                    Log.d("파베 퀘스천즈", "11111");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Log.d("파베 후", "11111");

    }

    private DataPoint[] getDataPoint() {
        int sumArr[]=new int[questions.size()];
        int count[]=new int [questions.size()];
        int count2[]=new int [questions.size()];
        DataPoint[] data = new DataPoint[questions.size()];
        Log.d("dsfd",Integer.toString(answers.size()));

        for(int j=0; j<questions.size();j++)
        {
            count[j]=1;
            count2[j]=0;

            for(int k=0; k<answers.size();k++)
            {
                if(j==answers.get(k).questionNum){
                    sumArr[j]=sumArr[j]+answers.get(k).value;
                    count[j]=count[j]+count2[j];
                    count2[j]=count2[j]+1;

                }

            }
            Log.d("값!!"+j,Integer.toString(sumArr[j]));
            Log.d("카운트"+j,Integer.toString(count2[j]));
        }

        for(int i=0;i<questions.size();i++) {

            data[i] = new DataPoint(i, sumArr[i]/count[i]);


        }



        return data;
    }
}
