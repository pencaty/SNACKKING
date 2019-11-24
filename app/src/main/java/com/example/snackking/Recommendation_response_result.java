package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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

public class Recommendation_response_result extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btn1;
    //private ImageButton btn2;
    private ImageButton btn3;
    private ImageButton btn4;

    private ListView lv_respond;

    private Button but_accept;
    private TextView recom_no_result;

    private Recommendation_Adapter adapter;

    private static String IP_ADDRESS = "http://snack.dothome.co.kr/";
    private static String TAG = "snack_arrange";
    public String mJsonString;
    private String user_id;

    private ArrayList<Response_DataStructure> snack_respond_list; // 사람들의 답변 모음


    @Override
    protected void onCreate(Bundle savedInstanceState) { // recommend_user_id table을 가져와서 안에 있는 데이터를 전부 보여주기
        super.onCreate(savedInstanceState);              // + accept 버튼 누르면 recommend_user_table 삭제하고, chatroom에서 user_Id 있는 줄 삭제
        setContentView(R.layout.activity_recommendation_response_result);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        this.InitializeView();
        this.SetListener();

        settingList(); // snack_respond_list에 (추천해준 유저 이름, 과자 이름)이 저장되어 있음

        adapter = new Recommendation_Adapter(snack_respond_list, this);
        lv_respond.setAdapter(adapter);

        if(snack_respond_list != null) {
            recom_no_result.setVisibility(View.GONE);
            lv_respond.setVisibility(View.VISIBLE);
            but_accept.setVisibility(View.VISIBLE);
        }
        else {
            recom_no_result.setVisibility(View.VISIBLE);
            lv_respond.setVisibility(View.GONE);
            but_accept.setVisibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
    }

    private void settingList(){ // 검색에 사용될 데이터를 리스트에 추가한다.

        Get_responses task = new Get_responses();

        try {
            mJsonString = task.execute(IP_ADDRESS + "/get_individual_responses.php", user_id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        showResult();
    }

    private class Get_responses extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Recommendation_response_result.this,
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
            String user = (String)params[1];

            String postParameters = "user_id=" + user;

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
        String TAG_USER = "user";
        String TAG_SNACK = "snack";

        if(mJsonString != null) {
            try {

                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                snack_respond_list = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    String respond_user = item.getString(TAG_USER);
                    String respond_snack = item.getString(TAG_SNACK);

                    Response_DataStructure response = new Response_DataStructure();
                    response.setuser(respond_user);
                    response.setsnack(respond_snack);

                    snack_respond_list.add(response); // 나중에 snack_datastructure로 바꾸면?

                    System.out.println(response.getsnack());
                    System.out.println(response.getuser());
                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }

    public void InitializeView() {
        btn1 = findViewById(R.id.imageButton1);
        //btn2 = findViewById(R.id.imageButton2);
        btn3 = findViewById(R.id.imageButton4);
        btn4 = findViewById(R.id.imageButton3);

        lv_respond = findViewById(R.id.listview_respond);
        but_accept = findViewById(R.id.button_accept);
        recom_no_result = (TextView)findViewById(R.id.Text_recom_no_result);


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
        }
    }

    public void SetListener() {
        btn1.setOnClickListener(this);
        //btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

    }
}
