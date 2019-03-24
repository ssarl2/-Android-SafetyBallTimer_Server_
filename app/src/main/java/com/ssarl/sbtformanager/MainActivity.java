package com.ssarl.sbtformanager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    private static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAADuGboLA:APA91bFhMrnBhEVnb_qIHEIxbHk-aDKf05zYSyk-dLu7GlPK0OoGuQrTLDERtuyF1Ak2xcdJC5nQtGFzb_45zRyWsAC8xh8Ayn6n_kDsjGZFDVLx99bowzu77qvX5uPKlwYf9wkzqr1s";
    private DatabaseReference tokenDatabase;
    private DatabaseReference questionsDatabase;
    int tokenCount = 0;
    //int questionCount = 0;
    int randomNumberForQuestions = 0;
    int random;
    Button btnn, makeQuestion;
    TextView tvv1, tvv2, tvv3;
    EditText ett1, ett2;
    String questionNum;
    String question;
    String validTime;
    List<String> tokenHouse = new ArrayList<>();
    List<Question> questionList = new ArrayList<>(); // Question 클래스를 담을 List 변수 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // START Get Data from Firebase server
        tokenDatabase = FirebaseDatabase.getInstance().getReference();

        // START Get all token from Firebase server
        if (tokenDatabase.child("gettoken").getKey() != null) {// 유저 존재여부 확인
            tokenDatabase.child("gettoken").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {//파베에 있는 토큰 값 받기
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//데이터 전체를 받기위해 반복문 실행
                        tokenHouse.add(snapshot.getValue().toString());// 토큰배열에 넣기
                        Log.d("토큰 값 : " + tokenCount, tokenHouse.get(tokenCount));
                        tokenCount++;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else { // 존재하지 않으면 Toast 메시지 띄움
            Toast.makeText(getApplicationContext(), "In current, there is no user in database.", Toast.LENGTH_LONG).show();
        }
        // END Get all token from Firebase server

        // START Get Question from Firebase server
        questionsDatabase = FirebaseDatabase.getInstance().getReference();
        if (questionsDatabase.child("Question").getKey() != null) {// 질문 존재여부 확인

            questionList.clear();

            questionsDatabase.child("Question").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Question qClass; // Question 클래스 초기화

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        qClass = snapshot.getValue(Question.class); // 파이어베이스에서 데이터를 Question 클래스 변수 qClass에 담기
                        questionList.add(qClass); // list에 data 추가.
                        //questionCount++; // 질문 개수 카운트
                    }

                    Random rnd = new Random(); // 랜덤 함수 초기화
                    //randomNumberForQuestions = rnd.nextInt(questionCount) + 1; // 0부터 tokenCount-1 까지 난수 생성 후 index 에 담기
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else { // 존재하지 않으면 Toast 메시지 띄움
            Toast.makeText(getApplicationContext(), "In current, there is no one tokenHouse in database.", Toast.LENGTH_LONG).show();
        }
        // END Get Get Question from Firebase server
        // END Get Data from Firebase server

        btnn = (Button) findViewById(R.id.btn);
        makeQuestion = (Button)findViewById(R.id.btn2);
        ett1 = (EditText) findViewById(R.id.et1);
        ett2 = (EditText) findViewById(R.id.et2);
        tvv1 = (TextView) findViewById(R.id.tv1);
        tvv2 = (TextView) findViewById(R.id.tv2);
        tvv3 = (TextView) findViewById(R.id.tv3);

        //question = ett1.getText().toString();
        //question = questionList.get(0).question_num + " : " + questionList.get(0).question_content;
        //validTime = ett2.getText().toString();

        btnn.setOnClickListener(this);
        makeQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MakeQuestionsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        questionNum = questionList.get(randomNumberForQuestions).question_num;
        question = questionList.get(randomNumberForQuestions).question_content;
        validTime = ett2.getText().toString();

        Log.e("onCreate: ", questionNum+ " : " + question);
        tvv1.setText(question);
        tvv2.setText(validTime);
        //tvv3.setText("{\"question\":\"" + question + "\",\"validTime\":\"" + validTime + "\"}");

        (new Thread(this)).start();
    }


    //final int time = questionCount * 5; // 상수 time은 tokenCount*5로 초기화
    // Delay(time); // 사용자 지정 함수 Delay(time)을 호출

    // 데이터 전송 쓰레드 시작
    @Override
    public void run() {
        try {
            Random rand = new Random();//랜덤유저 선택
            random = rand.nextInt(tokenCount);
            String mToken;
            mToken = tokenHouse.get(random);//토큰저장하는 변수에 랜덤 토큰 값 넣기
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(random + ".st");
            // FMC 메시지 생성 start
            JSONObject root = new JSONObject();
            JSONObject data = new JSONObject();

            data.put("questionNum", questionNum);
            data.put("question", question);
            data.put("validTime", validTime);
            data.put("title", getString(R.string.app_name));
            root.put("data", data);
            root.put("to", mToken);
            // FMC 메시지 생성 end
            URL Url = new URL(FCM_MESSAGE_URL);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(root.toString().getBytes("utf-8"));
            os.flush();
            conn.getResponseCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 데이터 전송 쓰레드 종료
}
