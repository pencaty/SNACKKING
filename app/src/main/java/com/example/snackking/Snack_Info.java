package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.icu.text.SymbolTable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.Intent;

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

public class Snack_Info extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<Snack_DataStructure> list;          // 데이터를 넣은 리스트변수
    private ArrayList<Snack_DataStructure> snack_list;          // 데이터를 넣은 리스트변수
    private static String IP_ADDRESS = "http://snack.dothome.co.kr/";
    private static String TAG = "snack_arrange";
    public String mJsonString;

    private String snack_name;
    private String snack_taste;
    private String snack_cost;
    private String snack_number_of_rate;
    private String user_id;

    private String past_data;

    private String have_reviewed = "0";

    //ImageButton btn1;
    ImageButton btn2;
    ImageButton btn3;
    ImageButton btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_info);

        this.InitializeView();
        this.SetListener();

        TextView info_snack_name = (TextView)findViewById(R.id.info_snack_name);
        TextView info_snack_taste = (TextView)findViewById(R.id.info_snack_taste);
        TextView info_snack_cost = (TextView)findViewById(R.id.info_snack_cost);

        TextView first_key = (TextView)findViewById(R.id.Text_first_keyword);
        TextView second_key = (TextView)findViewById(R.id.Text_second_keyword);
        TextView third_key = (TextView)findViewById(R.id.Text_third_keyword);

        Intent intent = getIntent();

        user_id = intent.getStringExtra("user_id");
        snack_name = intent.getStringExtra("name");
        snack_taste = intent.getStringExtra("taste");
        snack_cost = intent.getStringExtra("cost");
        snack_number_of_rate = intent.getStringExtra("number");

        Button button_review = (Button)findViewById(R.id.button_write_review);

        // user_id에 해당하는 테이블에 접속해서 현재 과자에 이미 리뷰를 남겼다면 버튼(review_write_review)의 텍스트를 '리뷰 수정'으로 바꾼다
        // 이미 리뷰를 남겼다면 have_reviewed = 1로 바꾸고, taste, cost, 키워드 선택한 것들을 하나로 저장하여 Snack_Review로 넘기자.
        // 리뷰 페이지에 접속했을 때 과거의 데이터가 그대로 보이도록.

        info_snack_name.setText(snack_name);
        info_snack_taste.setText(snack_taste);
        info_snack_cost.setText(snack_cost);

        past_data = "";

        ProgressBar pb1 =(ProgressBar)findViewById(R.id.progress_key1);
        ProgressBar pb2 =(ProgressBar)findViewById(R.id.progress_key2);
        ProgressBar pb3 =(ProgressBar)findViewById(R.id.progress_key3);

        setting_snack_List(); // 과자 키워드의 progress bar를 만들기 위해 정보를 가져오는 장치.
        Snack_DataStructure score_snack = snack_list.get(0);
        if(score_snack.getSnack_keyword_1() != null) first_key.setText(score_snack.getSnack_keyword_1());
        if(score_snack.getSnack_keyword_2() != null) second_key.setText(score_snack.getSnack_keyword_2());
        if(score_snack.getSnack_keyword_3() != null) third_key.setText(score_snack.getSnack_keyword_3());

        pb1.setMax(Integer.parseInt(score_snack.getSnack_keyword_1_score()));
        pb2.setMax(Integer.parseInt(score_snack.getSnack_keyword_1_score()));
        pb3.setMax(Integer.parseInt(score_snack.getSnack_keyword_1_score()));

        pb1.setVisibility(View.VISIBLE);
        pb2.setVisibility(View.VISIBLE);
        pb3.setVisibility(View.VISIBLE);
        first_key.setVisibility(View.VISIBLE);
        second_key.setVisibility(View.VISIBLE);
        third_key.setVisibility(View.VISIBLE);

        if(score_snack.getSnack_keyword_1_score() == null || score_snack.getSnack_keyword_1_score().equals("null")) {
            pb1.setVisibility(View.GONE);
            first_key.setVisibility(View.GONE);
        }
        else pb1.setProgress(Integer.parseInt(score_snack.getSnack_keyword_1_score()));

        if(score_snack.getSnack_keyword_2_score() == null || score_snack.getSnack_keyword_2_score().equals("null")) {
            pb2.setVisibility(View.GONE);
            second_key.setVisibility(View.GONE);
        }
        else pb2.setProgress(Integer.parseInt(score_snack.getSnack_keyword_2_score()));

        if(score_snack.getSnack_keyword_3_score() == null || score_snack.getSnack_keyword_3_score().equals("null")) {
            pb3.setVisibility(View.GONE);
            third_key.setVisibility(View.GONE);
        }
        else pb3.setProgress(Integer.parseInt(score_snack.getSnack_keyword_3_score()));

        // 키워드가 하나, 두개밖에 없는 경우도 고려해야함

        info_snack_taste.setText(score_snack.getSnack_taste());
        info_snack_cost.setText(score_snack.getSnack_cost());

        settingList(); // 하고 나면 과거에 리뷰를 남겼다면 list에 하나의 snack_datastructure가 있을 것이고, 남기지 않았다면 아무것도 없다.

        button_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent review_intent = new Intent(getApplicationContext(), Snack_Review.class);
                review_intent.putExtra(("name"), snack_name);
                review_intent.putExtra(("taste"), snack_taste);
                review_intent.putExtra(("cost"), snack_cost);
                review_intent.putExtra(("number"), snack_number_of_rate);
                review_intent.putExtra(("have_reviewed"), have_reviewed);
                review_intent.putExtra(("past_data"), past_data);
                review_intent.putExtra(("user_id"), user_id );
                startActivity(review_intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        Button button_review = (Button)findViewById(R.id.button_write_review);

        past_data = "";
        have_reviewed = "0";

        TextView first_key = (TextView)findViewById(R.id.Text_first_keyword);
        TextView second_key = (TextView)findViewById(R.id.Text_second_keyword);
        TextView third_key = (TextView)findViewById(R.id.Text_third_keyword);

        TextView info_snack_taste = (TextView)findViewById(R.id.info_snack_taste);
        TextView info_snack_cost = (TextView)findViewById(R.id.info_snack_cost);

        ProgressBar pb1 =(ProgressBar)findViewById(R.id.progress_key1);
        ProgressBar pb2 =(ProgressBar)findViewById(R.id.progress_key2);
        ProgressBar pb3 =(ProgressBar)findViewById(R.id.progress_key3);

        setting_snack_List(); // 과자 키워드의 progress bar를 만들기 위해 정보를 가져오는 장치.
        Snack_DataStructure score_snack = snack_list.get(0);
        if(score_snack.getSnack_keyword_1() != null) first_key.setText(score_snack.getSnack_keyword_1());
        if(score_snack.getSnack_keyword_2() != null) second_key.setText(score_snack.getSnack_keyword_2());
        if(score_snack.getSnack_keyword_3() != null) third_key.setText(score_snack.getSnack_keyword_3());

        pb1.setMax(Integer.parseInt(score_snack.getSnack_keyword_1_score()));
        pb2.setMax(Integer.parseInt(score_snack.getSnack_keyword_1_score()));
        pb3.setMax(Integer.parseInt(score_snack.getSnack_keyword_1_score()));

        pb1.setVisibility(View.VISIBLE);
        pb2.setVisibility(View.VISIBLE);
        pb3.setVisibility(View.VISIBLE);
        first_key.setVisibility(View.VISIBLE);
        second_key.setVisibility(View.VISIBLE);
        third_key.setVisibility(View.VISIBLE);

        if(score_snack.getSnack_keyword_1_score() == null || score_snack.getSnack_keyword_1_score().equals("null")) {
            pb1.setVisibility(View.GONE);
            first_key.setVisibility(View.GONE);
        }
        else pb1.setProgress(Integer.parseInt(score_snack.getSnack_keyword_1_score()));

        if(score_snack.getSnack_keyword_2_score() == null || score_snack.getSnack_keyword_2_score().equals("null")) {
            //pb2.setProgress(0);
            pb2.setVisibility(View.GONE);
            second_key.setVisibility(View.GONE);
        }
        else pb2.setProgress(Integer.parseInt(score_snack.getSnack_keyword_2_score()));

        if(score_snack.getSnack_keyword_3_score() == null || score_snack.getSnack_keyword_3_score().equals("null")) {
            pb3.setVisibility(View.GONE);
            third_key.setVisibility(View.GONE);
        }
        else pb3.setProgress(Integer.parseInt(score_snack.getSnack_keyword_3_score()));

        // 키워드가 하나, 두개밖에 없는 경우도 고려해야함

        info_snack_taste.setText(score_snack.getSnack_taste());
        info_snack_cost.setText(score_snack.getSnack_cost());

        settingList();

        button_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent review_intent = new Intent(getApplicationContext(), Snack_Review.class);
                review_intent.putExtra(("name"), snack_name);
                review_intent.putExtra(("taste"), snack_taste);
                review_intent.putExtra(("cost"), snack_cost);
                review_intent.putExtra(("number"), snack_number_of_rate);
                review_intent.putExtra(("have_reviewed"), have_reviewed);
                review_intent.putExtra(("past_data"), past_data);
                review_intent.putExtra(("user_id"), user_id );
                startActivity(review_intent);
            }
        });
    }


    private void settingList(){ // 검색에 사용될 데이터를 리스트에 추가한다.

        Get_user_individual_data task = new Get_user_individual_data();
        list = new ArrayList<>();

        try {
            mJsonString = task.execute(IP_ADDRESS + "get_user_individual_data.php", user_id, snack_name).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        showResult();
    }

    class Get_user_individual_data extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Snack_Info.this,
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
            String snack_name = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "user_id=" + user_id + "&snack_name=" + snack_name;

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
        String TAG_TASTE = "taste";
        String TAG_COST = "cost";
        String TAG_KEYWORD1 = "keyword1";
        String TAG_KEYWORD2 = "keyword2";
        String TAG_KEYWORD3 = "keyword3";

        if(mJsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);

                    String taste = item.getString(TAG_TASTE);
                    String cost = item.getString(TAG_COST);
                    String keyword_1 = item.getString(TAG_KEYWORD1);
                    String keyword_2 = item.getString(TAG_KEYWORD2);
                    String keyword_3 = item.getString(TAG_KEYWORD3);

                    Snack_DataStructure snackdata = new Snack_DataStructure();

                    snackdata.setSnack_taste(taste);
                    snackdata.setSnack_cost(cost);
                    snackdata.setSnack_keyword_1(keyword_1);
                    snackdata.setSnack_keyword_2(keyword_2);
                    snackdata.setSnack_keyword_3(keyword_3);

                    list.add(snackdata); // 유저가 각 과자에 매긴 점수가 다 담겨 있는 리스트

                    Button button_review = (Button) findViewById(R.id.button_write_review);
                    have_reviewed = "1";
                    button_review.setText("Revise Review");
                    Snack_DataStructure past_snack = list.get(0); //이 과자에 대해 리뷰했다면 list에는 하나밖에 없고, 리뷰하지 않았다면 텅 빈 list이다
                    past_data = past_snack.getSnack_taste() + "#" + past_snack.getSnack_cost() + "#" + past_snack.getSnack_keyword_1() + "#" + past_snack.getSnack_keyword_2() + "#" + past_snack.getSnack_keyword_3();
                    System.out.println(past_data);
                    System.out.println("end of Show_Result in Snack_info");
                    // 과거 taste, cost, keyword1,2,3 에 대해 입력했던 값을 Snack_Review로 전달하기 위해.
                    // -> Snack_Review에서 past_data를 split("#")을 하면 각각의 데이터를 얻을 수 있다.

                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }

    private void setting_snack_List(){ // 검색에 사용될 데이터를 리스트에 추가한다.

        GetData task = new GetData();

        try {
            mJsonString = task.execute(IP_ADDRESS + "/get_snack_keyword_score.php", snack_name).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(snack_name);
        System.out.println(mJsonString);
        System.out.println("milkcrunky");

        show_snack_Result();
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Snack_Info.this,
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

            String snack_name = (String)params[1];

            String serverURL = (String)params[0];
            String postParameters = "snack_name=" + snack_name;

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

    private void show_snack_Result(){

        String TAG_JSON="snack_json";
        String TAG_TASTE = "taste";
        String TAG_COST = "cost";
        String TAG_KEYWORD1 = "keyword1";
        String TAG_KEYWORD2 = "keyword2";
        String TAG_KEYWORD3 = "keyword3";
        String TAG_KEYWORD1_score = "keyword1_score";
        String TAG_KEYWORD2_score = "keyword2_score";
        String TAG_KEYWORD3_score = "keyword3_score";

        if(mJsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(mJsonString); // 현재 과자에 대한 정보를 가져오는 것이므로 항상 하나 존재
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    String taste = item.getString(TAG_TASTE);
                    String cost = item.getString(TAG_COST);
                    String keyword_1 = item.getString(TAG_KEYWORD1);
                    String keyword_2 = item.getString(TAG_KEYWORD2);
                    String keyword_3 = item.getString(TAG_KEYWORD3);
                    String keyword1_score = item.getString(TAG_KEYWORD1_score);
                    String keyword2_score = item.getString(TAG_KEYWORD2_score);
                    String keyword3_score = item.getString(TAG_KEYWORD3_score);

                    Snack_DataStructure snackdata = new Snack_DataStructure();
                    snack_list = new ArrayList<>();
                    snackdata.setSnack_taste(taste);
                    snackdata.setSnack_cost(cost);
                    snackdata.setSnack_keyword_1(keyword_1);
                    snackdata.setSnack_keyword_2(keyword_2);
                    snackdata.setSnack_keyword_3(keyword_3);
                    snackdata.setSnack_keyword_1_score(keyword1_score);
                    snackdata.setSnack_keyword_2_score(keyword2_score);
                    snackdata.setSnack_keyword_3_score(keyword3_score);
                    snack_list.add(snackdata);
                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }

    public void InitializeView() {
        //btn1 = findViewById(R.id.imageButton);
        btn2 = findViewById(R.id.imageButton2);
        btn3 = findViewById(R.id.imageButton3);
        btn4 = findViewById(R.id.imageButton4);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.imageButton1:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;*/
            case R.id.imageButton2:
                Intent intent = new Intent(this, Recommendation.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                overridePendingTransition(0, 0);
                this.finish();
                break;
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
        //btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }
}
