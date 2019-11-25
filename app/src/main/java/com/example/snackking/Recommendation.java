package com.example.snackking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class Recommendation extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageButton btn1;
    //private ImageButton btn2;
    private ImageButton btn3;
    private ImageButton btn4;

    private Button button_recommend;
    private Button button_respond;

    private int count = 0;
    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;
    private CheckBox cb4;
    private CheckBox cb5;
    private CheckBox cb6;
    private CheckBox cb7;
    private CheckBox cb8;

    private EditText commt;

    private static String IP_ADDRESS = "http://snack.dothome.co.kr/";
    private static String TAG = "snack_arrange";
    public String mJsonString;
    private String user_id;
    private ArrayList<String> keyword_list; // 선택된 키워드 (max 3) 저장
    private ArrayList<String> request_id_list; // request를 보낸 사람들의 id를 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        this.InitializeView();
        this.SetListener();

        settingList(); // 다녀오면 request_id_list에는 이전에 request를 보냈던 사람들의 id가 저장되어 있다.
        // 여기에 현재 유저의 id가 있냐없냐에 따라 나뉨

        if(request_id_list != null && request_id_list.contains(user_id)) {
            //해당 request에 대한 respond를 보여주는 페이지로 바로 이동
            Intent send_intent = new Intent(getApplicationContext(), Recommendation_response_result.class);
            send_intent.putExtra("user_id", user_id);
            startActivity(send_intent);
            this.finish();
        }
        else { // request를 보낸 적이 없음. --> 현재 페이지에서 request를 보내거나 다른 사람의 request에 대답할 수 있도록

            button_recommend = (Button) findViewById(R.id.button_recommend); // chatroom table에 추가, recommend_user_id table 만들기
            button_respond = (Button) findViewById(R.id.button_recommend_respond); // recommend를 요청한 것들을 보여주기 + skip 버튼 페이지로 이동

            button_recommend.setOnClickListener(this);
            button_respond.setOnClickListener(this);

        }
    }

    private void settingList(){ // 검색에 사용될 데이터를 리스트에 추가한다.

        Get_chatroom task = new Get_chatroom();

        try {
            mJsonString = task.execute(IP_ADDRESS + "/get_chatroom_data.php", "").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        showResult();
    }

    private class Get_chatroom extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Recommendation.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

        }

        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

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
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }
        }
    }

    private void showResult(){

        String TAG_JSON="snack_json";
        String TAG_REQUEST_ID = "request_id";
        /*String TAG_KEYWORD1 = "keyword1";
        String TAG_KEYWORD2 = "keyword2";
        String TAG_KEYWORD3 = "keyword3";
        String TAG_COMMENT = "comment";*/

        if(mJsonString != null) {
            try {

                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                request_id_list = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    String request_id = item.getString(TAG_REQUEST_ID);
                    /*String keyword_1 = item.getString(TAG_KEYWORD1);
                    String keyword_2 = item.getString(TAG_KEYWORD2);
                    String keyword_3 = item.getString(TAG_KEYWORD3);
                    String comment = item.getString(TAG_COMMENT);*/

                    request_id_list.add(request_id);
                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }

    class Request_recommend extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Recommendation.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
            finish();
        }


        @Override
        protected String doInBackground(String... params) {
            //snack_name, taste_score, cost_score, sweet_score, spicy_score, sour_score, bitter_score).get();
            String user_id = (String)params[1];
            String key1 = (String)params[2];
            String key2 = (String)params[3];
            String key3 = (String)params[4];
            String comment = (String)params[5];

            String serverURL = (String)params[0];
            String postParameters = "user_id=" + user_id + "&keyword_1=" + key1 + "&keyword_2=" + key2 + "&keyword_3=" + key3 + "&comment=" + comment;

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
                return new String("Error: " + e.getMessage());
            }
        }
    }

    public void InitializeView() {
        btn1 = findViewById(R.id.imageButton1);
        //btn2 = findViewById(R.id.imageButton2);
        btn3 = findViewById(R.id.imageButton4);
        btn4 = findViewById(R.id.imageButton3);

        cb1 = (CheckBox)findViewById(R.id.checkbox_recom_key1);
        cb2 = (CheckBox)findViewById(R.id.checkbox_recom_key2);
        cb3 = (CheckBox)findViewById(R.id.checkbox_recom_key3);
        cb4 = (CheckBox)findViewById(R.id.checkbox_recom_key4);
        cb5 = (CheckBox)findViewById(R.id.checkbox_recom_key5);
        cb6 = (CheckBox)findViewById(R.id.checkbox_recom_key6);
        cb7 = (CheckBox)findViewById(R.id.checkbox_recom_key7);
        cb8 = (CheckBox)findViewById(R.id.checkbox_recom_key8);

        commt = (EditText)findViewById(R.id.edittext_recommend_comment);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton1:
                Intent intent = new Intent(this, Search_Combined.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                overridePendingTransition(0, 0);
                this.finish();
                break;
                /*
            case R.id.imageButton2:
                intent = new Intent(this, Recommendation.class);
                startActivity(intent);
                break;*/
            case R.id.imageButton3:
                intent = new Intent(this, Achievement.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                overridePendingTransition(0, 0);
                this.finish();
                break;
            case R.id.imageButton4:
                intent = new Intent(this, Setting.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                overridePendingTransition(0, 0);
                this.finish();
                break;

            case R.id.button_recommend: // keyword에 해당하는 과자 추천을 request할 때
                if (count == 0) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Select at least one keyword", Toast.LENGTH_SHORT);
                    int offSetX = 0;
                    int offSetY = 0;
                    toast.setGravity(Gravity.CENTER, offSetX, offSetY);
                    toast.show();
                    break;
                } else { // make_individual_recommend.php로 연결 (user_id, keyword_1, keyword_2, keyword_3, comment)
                    while(keyword_list.size() < 3) {
                        keyword_list.add(" ");
                    }

                    Request_recommend review_data = new Request_recommend();

                    try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                        String comment = commt.getText().toString();
                        if(comment == null || comment.equals(" ") || comment.equals("")) comment = "None";
                        //String res = review_data.execute(IP_ADDRESS + "/make_individual_recommend.php", user_id, keyword_list.get(0), keyword_list.get(1), keyword_list.get(2), commt.getText().toString()).get();
                        String res = review_data.execute(IP_ADDRESS + "/make_individual_recommend.php", user_id, keyword_list.get(0), keyword_list.get(1), keyword_list.get(2), comment).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent send_intent = new Intent(getApplicationContext(), Recommendation_response_result.class);
                    send_intent.putExtra("user_id", user_id);
                    startActivity(send_intent);
                    this.finish();
                    break;
                }
            case R.id.button_recommend_respond: // 다른 사람들의 request에 대답할 때
                Intent send_intent = new Intent(getApplicationContext(), Recommendation_respond.class);
                send_intent.putExtra("user_id", user_id);
                startActivity(send_intent);
                this.finish();
                break;
        }
    }

    public void SetListener() {
        btn1.setOnClickListener(this);
        //btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);
        cb4.setOnCheckedChangeListener(this);
        cb5.setOnCheckedChangeListener(this);
        cb6.setOnCheckedChangeListener(this);
        cb7.setOnCheckedChangeListener(this);
        cb8.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        keyword_list = new ArrayList<>();

        count = 0;
        if(cb1.isChecked()) {
            count++;
            keyword_list.add(cb1.getText().toString());
        }
        if(cb2.isChecked()) {
            count++;
            keyword_list.add(cb2.getText().toString());
        }
        if(cb3.isChecked()) {
            count++;
            keyword_list.add(cb3.getText().toString());
        }
        if(cb4.isChecked()) {
            count++;
            keyword_list.add(cb4.getText().toString());
        }
        if(cb5.isChecked()) {
            count++;
            keyword_list.add(cb5.getText().toString());
        }
        if(cb6.isChecked()) {
            count++;
            keyword_list.add(cb6.getText().toString());
        }
        if(cb7.isChecked()) {
            count++;
            keyword_list.add(cb7.getText().toString());
        }
        if(cb8.isChecked()) {
            count++;
            keyword_list.add(cb8.getText().toString());
        }

        if(count == 3){
            if(!cb1.isChecked()) cb1.setEnabled(false);
            if(!cb2.isChecked()) cb2.setEnabled(false);
            if(!cb3.isChecked()) cb3.setEnabled(false);
            if(!cb4.isChecked()) cb4.setEnabled(false);
            if(!cb5.isChecked()) cb5.setEnabled(false);
            if(!cb6.isChecked()) cb6.setEnabled(false);
            if(!cb7.isChecked()) cb7.setEnabled(false);
            if(!cb8.isChecked()) cb8.setEnabled(false);
        }
        else{
            cb1.setEnabled(true);
            cb2.setEnabled(true);
            cb3.setEnabled(true);
            cb4.setEnabled(true);
            cb5.setEnabled(true);
            cb6.setEnabled(true);
            cb7.setEnabled(true);
            cb8.setEnabled(true);
        }
    }

}
