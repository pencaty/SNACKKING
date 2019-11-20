package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.icu.text.SymbolTable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Snack_Info extends AppCompatActivity {

    private ArrayList<Snack_DataStructure> list;          // 데이터를 넣은 리스트변수
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

    private int num = 0; // 1이 되면 review를 한 것이 됨

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_info);

        TextView info_snack_name = (TextView)findViewById(R.id.info_snack_name);
        TextView info_snack_taste = (TextView)findViewById(R.id.info_snack_taste);
        TextView info_snack_cost = (TextView)findViewById(R.id.info_snack_cost);

        Intent intent = getIntent();

        user_id = intent.getStringExtra("user_id");
        snack_name = intent.getStringExtra("name");
        snack_taste = intent.getStringExtra("taste");
        snack_cost = intent.getStringExtra("cost");
        snack_number_of_rate = intent.getStringExtra("number");

        Button button_review = (Button)findViewById(R.id.button_write_review);
        Button button_info_back = (Button)findViewById(R.id.button_info_back);

        // user_id에 해당하는 테이블에 접속해서 현재 과자에 이미 리뷰를 남겼다면 버튼(review_write_review)의 텍스트를 '리뷰 수정'으로 바꾼다
        // 이미 리뷰를 남겼다면 have_reviewed = 1로 바꾸고, taste, cost, 키워드 선택한 것들을 하나로 저장하여 Snack_Review로 넘기자.
        // 리뷰 페이지에 접속했을 때 과거의 데이터가 그대로 보이도록.

        info_snack_name.setText(snack_name);
        info_snack_taste.setText(snack_taste);
        info_snack_cost.setText(snack_cost);

        past_data = "";

        settingList();
        //if(!list.isEmpty()) { // 과거에 리뷰를 남겼다면 list에는 하나의 Snack_Datastruture가 존재할 것.
/*        if(num == 1) {
            Button button_review = (Button)findViewById(R.id.button_write_review);
            have_reviewed = "1";
            button_review.setText("Revise Review");
            Snack_DataStructure past_snack = list.get(0);
            past_data = past_snack.getSnack_taste() +"#"+past_snack.getSnack_cost()+"#"+past_snack.getSnack_keyword_1()+"#"+past_snack.getSnack_keyword_2()+"#"+past_snack.getSnack_keyword_3();
            // 과거 taste, cost, keyword1,2,3 에 대해 입력했던 값을 Snack_Review로 전달하기 위해.
            // -> Snack_Review에서 past_data를 split("#")을 하면 각각의 데이터를 얻을 수 있다.
        }*/

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

        button_info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        Button button_review = (Button)findViewById(R.id.button_write_review);
        Button button_info_back = (Button)findViewById(R.id.button_info_back);

        past_data = "";
        have_reviewed = "0";

        settingList();
        //if(!list.isEmpty()) {
/*        if(num == 1) {
            have_reviewed = "1";
            button_review.setText("Revise Review");
            Snack_DataStructure past_snack = list.get(0);
            past_data = past_snack.getSnack_taste() +"#"+past_snack.getSnack_cost()+"#"+past_snack.getSnack_keyword_1()+"#"+past_snack.getSnack_keyword_2()+"#"+past_snack.getSnack_keyword_3();
            // 과거 taste, cost, keyword1,2,3 에 대해 입력했던 값을 Snack_Review로 전달하기 위해.
            // -> Snack_Review에서 past_data를 split("#")을 하면 각각의 데이터를 얻을 수 있다.
        }*/

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

        button_info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void settingList(){ // 검색에 사용될 데이터를 리스트에 추가한다.

        Get_user_individual_data task = new Get_user_individual_data();
        list = new ArrayList<>();

        task.execute(IP_ADDRESS + "get_user_individual_data.php", user_id, snack_name);
        /*try {
            mJsonString = task.execute(IP_ADDRESS + "get_user_individual_data.php", user_id, snack_name).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        showResult();*/
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

            if(result != null) {
                mJsonString = result;
                showResult();
            }
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

        System.out.println("json from snack_info");
        System.out.println(mJsonString);

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){
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
                num = 1;
                list.add(snackdata); // 유저가 각 과자에 매긴 점수가 다 담겨 있는 리스트

                Button button_review = (Button)findViewById(R.id.button_write_review);
                have_reviewed = "1";
                button_review.setText("Revise Review");
                Snack_DataStructure past_snack = list.get(0);
                past_data = past_snack.getSnack_taste() +"#"+past_snack.getSnack_cost()+"#"+past_snack.getSnack_keyword_1()+"#"+past_snack.getSnack_keyword_2()+"#"+past_snack.getSnack_keyword_3();
                System.out.println(past_data);
                System.out.println("bibibibibibibbibibibibibibibi");
                // 과거 taste, cost, keyword1,2,3 에 대해 입력했던 값을 Snack_Review로 전달하기 위해.
                // -> Snack_Review에서 past_data를 split("#")을 하면 각각의 데이터를 얻을 수 있다.
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
