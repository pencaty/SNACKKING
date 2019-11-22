package com.example.snackking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Achievement extends AppCompatActivity implements View.OnClickListener {

    ImageButton btn1;
    ImageButton btn2;
    //ImageButton btn3;
    ImageButton btn4;

    private String user_id;

    private String review_num = "0";

    private static String IP_ADDRESS = "http://snack.dothome.co.kr/";
    private static String TAG = "snack_arrange";
    public String mJsonString;

    private Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        this.InitializeView();
        this.SetListener();

        settingList(); // 갔다오면 review_num에는 이전까지 리뷰를 단 과자의 개수가 int 형식으로 적혀있음

        sw =  (Switch) findViewById(R.id.switch1);

        CheckState();

        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckState();
            }
        });

    }

    public void InitializeView() {
        btn1 = findViewById(R.id.imageButton1);
        btn2 = findViewById(R.id.imageButton2);
        //btn3 = findViewById(R.id.imageButton4);
        btn4 = findViewById(R.id.imageButton4);
    }

    public void onClick(View view) {
        CheckState();
        switch (view.getId()) {
            case R.id.imageButton1:
                Intent intent = new Intent(this, Search_Combined.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                overridePendingTransition(0, 0);
                this.finish();
                break;
            case R.id.imageButton2:
                intent = new Intent(this, Recommendation.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                overridePendingTransition(0, 0);
                this.finish();
                break;
                /*
            case R.id.imageButton3:
                intent = new Intent(this, Achievement.class);
                startActivity(intent);
                break;
                */
            case R.id.imageButton4:
                intent = new Intent(this, Setting.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                overridePendingTransition(0, 0);
                this.finish();
                break;
        }
    }

    public void SetListener() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        //btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    private void CheckState() {

        if(!sw.isChecked()) {
            ListView listview;
            ListAdapter_Achievement adapter;

            adapter = new ListAdapter_Achievement();

            listview = (ListView) findViewById(R.id.listview1);
            listview.setAdapter(adapter);

            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 1", "Leave more than 1 review", review_num + " / 1", Integer.parseInt(review_num), 1);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 2", "Leave more than 3 reviews", review_num + " / 3", Integer.parseInt(review_num), 3);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 3", "Leave more than 5 reviews", review_num + " / 5", Integer.parseInt(review_num), 5);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 4", "Leave more than 10 reviews", review_num + " / 10", Integer.parseInt(review_num), 10);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 5", "Leave more than 20 reviews", review_num + " / 20", Integer.parseInt(review_num), 20);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 6", "Leave more than 30 reviews", review_num + " / 30", Integer.parseInt(review_num), 30);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 7", "Leave more than 50 reviews", review_num + " / 50", Integer.parseInt(review_num), 50);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 8", "Leave more than 100 reviews", review_num + " / 100", Integer.parseInt(review_num), 100);
            /*adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 9", "Leave more than 1 review", "15 / 90", 15, 90);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 10", "Leave more than 1 review", "15 / 100", 15, 100);*/

        }
        else {
            ListView listview;
            ListAdapter_Achievement adapter;

            adapter = new ListAdapter_Achievement();

            listview = (ListView) findViewById(R.id.listview1);
            listview.setAdapter(adapter);

            int review_num_int = Integer.parseInt(review_num);

            if(review_num_int >= 1) adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 1", "Leave more than 1 review", review_num + " / 1", Integer.parseInt(review_num), 1);
            if(review_num_int >= 3) adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 2", "Leave more than 3 reviews", review_num + " / 3", Integer.parseInt(review_num), 3);
            if(review_num_int >= 5) adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 3", "Leave more than 5 reviews", review_num + " / 5", Integer.parseInt(review_num), 5);
            if(review_num_int >= 10) adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 4", "Leave more than 10 reviews", review_num + " / 10", Integer.parseInt(review_num), 10);
            if(review_num_int >= 20) adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 5", "Leave more than 20 reviews", review_num + " / 20", Integer.parseInt(review_num), 20);
            if(review_num_int >= 30) adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 6", "Leave more than 30 reviews", review_num + " / 30", Integer.parseInt(review_num), 30);
            if(review_num_int >= 50) adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 7", "Leave more than 50 reviews", review_num + " / 50", Integer.parseInt(review_num), 50);
            if(review_num_int >= 100) adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Review Master 8", "Leave more than 100 reviews", review_num + " / 100", Integer.parseInt(review_num), 100);


        }
    }

    private void settingList(){ // 검색에 사용될 데이터를 리스트에 추가한다.

        Get_review_num task = new Get_review_num();

        try {
            mJsonString = task.execute(IP_ADDRESS + "get_review_num_for_achievement.php", user_id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        showResult();
    }

    class Get_review_num extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Achievement.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {

            String user_id = (String)params[1];

            String serverURL = (String)params[0];
            String postParameters = "user_id=" + user_id;

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();

            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return null;
                //return new String("Error: " + e.getMessage());
            }
        }
    }
    private void showResult(){

        String TAG_JSON="snack_json";
        String TAG_NUMBER = "number";

        if(mJsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                JSONObject item = jsonArray.getJSONObject(0);

                review_num = item.getString(TAG_NUMBER);

            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }

}

