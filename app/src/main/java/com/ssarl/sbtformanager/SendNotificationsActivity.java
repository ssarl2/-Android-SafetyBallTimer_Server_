package com.ssarl.sbtformanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.ssarl.sbtformanager.Object.Answers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class SendNotificationsActivity extends AppCompatActivity implements View.OnClickListener, Runnable{

    private final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private final String SERVER_KEY = "AAAA87HPu5I:APA91bEU-3xSr0Gl6UjZa1KdTuyRW3Yk8_PaCBLX97pLE7NnitsEyUvMwMJ67oXBdUDnNClrnYxxCmZkz9Ai3fy2X43WP0EGgw_0nu-YG49lnEjPmjnrs17YqG3AwZkxcTFHKL4zFxC_";


    int randomUserNumber;

    String validTime;
    String userToken;
    String selected_item;


    private ListView listView;

    private Button sendOnebtn;
    private Button sendAllbtn;

    private ArrayAdapter arrayAdapter;

    HashMap<String, Integer> questionMap;

    HashSet<Integer> removedItemsSet;


    private ArrayList<Answers> answersArrayList; // create List variable in order to put Question class Question 클래스를 담을 List 변수 생성
    private ArrayList<String> listViewArrayList;
    private ArrayList<String> tokenArrayList;

    private JSONArray userTokenArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notifications);


        Intent intent = getIntent();

        answersArrayList = (ArrayList<Answers>) intent.getSerializableExtra("answersArrayList");
        tokenArrayList = (ArrayList<String>) intent.getSerializableExtra("tokenArrayList");

        sendOnebtn = findViewById(R.id.sendOneBtn);
        sendAllbtn = findViewById(R.id.sendAllBtn);

        listView = (ListView) findViewById(R.id.listviewSendNotifications);

        listViewArrayList = new ArrayList<>();
        questionMap = new HashMap<>();
        removedItemsSet = new HashSet<>();
        userTokenArrayList = new JSONArray();


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
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // to make view choose one item

        // input checkitems from listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_item = (parent.getItemAtPosition(position).toString());
            }
        });


        sendOnebtn.setOnClickListener(this);

        sendAllbtn.setOnClickListener(this);


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

        if(selected_item == null) {
            return;
        }
        validTime = Integer.toString(30);
        if (validTime.equals(""))
            return; // if empty time is put, make it Invalidation 빈 시간이 들어갔을 경우 무효화 처리

        switch (v.getId()){
            case R.id.sendOneBtn:

                Random random = new Random(); // set random function 랜덤 함수 초기화
                random.setSeed(System.currentTimeMillis()); // set seed. remove what only same value come out when app is executed 시드 설정. 어플을 실행시켰을 시 같은 값만 나오는 것을 제거

                randomUserNumber = random.nextInt(tokenArrayList.size() + 0);
                userToken = tokenArrayList.get(randomUserNumber); // put random value of token in variable of storage of token 토큰저장하는 변수에 랜덤 토큰 값 넣기

                break;
            case R.id.sendAllBtn:

                for(String str : tokenArrayList){
                    userTokenArrayList.put(str);
                }

                break;
        }

        (new Thread(this)).start();

    }

    //final int time = questionCount * 5; // 상수 time은 tokenCount*5로 초기화
    // Delay(time); // 사용자 지정 함수 Delay(time)을 호출


    // START Thread Data Transfer
    @Override
    public void run() {
        try {
            // FMC Create message START
            JSONObject root = new JSONObject();
            JSONObject data = new JSONObject();

            data.put("question", selected_item);
            data.put("validTime", validTime);
            data.put("title", getString(R.string.app_name));
            root.put("data", data);
            if(userTokenArrayList.length() == 0){ // send notifications to single or multiple
                root.put("to", userToken);
            } else {
                root.put("registration_ids", userTokenArrayList);
            }
            // FMC Create message END
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

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
        finish();
    }

    // If there is no this method, it doesn't work after deleting any data
    @Override
    protected void onPause() {
        tokenArrayList.clear();
        answersArrayList.clear();
        removedItemsSet.clear();
        arrayAdapter.clear();

        super.onPause();
    }

}