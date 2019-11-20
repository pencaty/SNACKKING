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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Snack_Review extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static String IP_ADDRESS = "http://snack.dothome.co.kr/";
    private static String TAG = "Insert_data";

    private String snack_name;
    private String snack_taste;
    private String snack_cost;
    private String snack_number_of_rate;

    private EditText EditTaste;
    private EditText EditCost;
    private TextView Text_SnackName;

    private TextView Text_taste;
    private TextView Text_cost;
    private TextView Text_keyword1;
    private TextView Text_keyword2;
    private TextView Text_keyword3;

    private EditText Editkeyword1;
    private EditText Editkeyword2;
    private EditText Editkeyword3;

    private String have_reviewed;
    private String past_data;
    private String user_id;

    private int count = 0;
    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;
    private CheckBox cb4;

    private ArrayList<String> keyword_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_review);

        Intent intent = getIntent();

        Text_SnackName = (TextView)findViewById(R.id.review_snack_name);
        Text_taste = (TextView)findViewById(R.id.review_taste_name);
        Text_cost = (TextView)findViewById(R.id.review_cost_name);
        /*Text_keyword1 = (TextView)findViewById(R.id.review_keyword1_name);
        Text_keyword2 = (TextView)findViewById(R.id.review_keyword2_name);
        Text_keyword3 = (TextView)findViewById(R.id.review_keyword3_name);*/

        keyword_list = new ArrayList<>();

        cb1 = (CheckBox)findViewById(R.id.checkbox_review_key1);
        cb2 = (CheckBox)findViewById(R.id.checkbox_review_key2);
        cb3 = (CheckBox)findViewById(R.id.checkbox_review_key3);
        cb4 = (CheckBox)findViewById(R.id.checkbox_review_key4);

        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);
        cb4.setOnCheckedChangeListener(this);

        EditTaste = (EditText)findViewById(R.id.review_taste);
        EditCost = (EditText)findViewById(R.id.review_cost);

        /*Editkeyword1 = (EditText)findViewById((R.id.review_keyword1));
        Editkeyword2 = (EditText)findViewById((R.id.review_keyword2));
        Editkeyword3 = (EditText)findViewById((R.id.review_keyword3));*/

        snack_name = intent.getStringExtra("name");
        snack_taste = intent.getStringExtra("taste");
        snack_cost = intent.getStringExtra("cost");
        snack_number_of_rate = intent.getStringExtra("number");

        have_reviewed = intent.getStringExtra("have_reviewed");
        past_data = intent.getStringExtra("past_data");
        user_id = intent.getStringExtra("user_id");

        Text_SnackName.setText(snack_name);

        System.out.println("starto f snack review~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(have_reviewed);

        final Button button_upload = (Button)findViewById(R.id.button_upload_review);

        // 맨 처음에 버튼 비활성화 추가하기
        button_upload.setEnabled(false);

        if(have_reviewed.equals("0")) { // 과거에 리뷰를 단 적이 없는 경우
            button_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String taste_score = EditTaste.getText().toString();
                    String cost_score = EditCost.getText().toString();

                    System.out.println("past review no");

                    double double_taste_score = Double.parseDouble(taste_score);
                    double double_cost_score = Double.parseDouble(cost_score);
                    double double_number_of_rate = Double.parseDouble(snack_number_of_rate);

                    String average_taste = String.format("%.2f", (Double.parseDouble(snack_taste) * double_number_of_rate + double_taste_score) / (double_number_of_rate + 1));
                    String average_cost = String.format("%.2f", (Double.parseDouble(snack_cost) * double_number_of_rate + double_cost_score) / (double_number_of_rate + 1));

                    UpdateData task = new UpdateData();

                    System.out.println("SNack review 126 ~~~~~~~~ " + average_taste);
                    System.out.println("SNack review 126 ~~~~~~~~ " + average_cost);

                    try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                        String res = task.execute(IP_ADDRESS + "/update_score.php", snack_name, average_taste, average_cost, Double.toString(double_number_of_rate + 1)).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    System.out.println("wowowowwowowowowowowow");
                    System.out.println(keyword_list.get(0));

                    /*keyword_list.add(Editkeyword1.getText().toString().toLowerCase());
                    keyword_list.add(Editkeyword2.getText().toString().toLowerCase());
                    keyword_list.add(Editkeyword3.getText().toString().toLowerCase());*/

                    String sweet_score = "0";
                    String spicy_score = "0";
                    String sour_score = "0";
                    String bitter_score = "0";

                    if (keyword_list.contains("sweet")) sweet_score = "1";
                    if (keyword_list.contains("spicy")) spicy_score = "1";
                    if (keyword_list.contains("sour")) sour_score = "1";
                    if (keyword_list.contains("bitter")) bitter_score = "1";

                    UpdateKeyScore task_key = new UpdateKeyScore();

                    System.out.println("Sweet review 154 ~~~~~~~~ " + sweet_score);
                    System.out.println("Spicyreview 154 ~~~~~~~~ " + spicy_score);
                    System.out.println("Sour review 154 ~~~~~~~~ " + sour_score);
                    System.out.println("Bitter review 154 ~~~~~~~~ " + bitter_score);

                    try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                        String res = task_key.execute(IP_ADDRESS + "/update_snack.php", snack_name, sweet_score, spicy_score, sour_score, bitter_score).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // keyword 별 개수 가장 많은 순으로 3개를 Keyword_One, Two, Three에 넣자.
                    // --> UpdateKeyScore 관련 php에서 모두 다 끝난 후 하는 것으로.

                    // 이제 유저 개개인 테이블에 과자, 점수, 키워드를 저장   .

                    while(keyword_list.size() < 3) {
                        keyword_list.add("-");
                    }

                    Review_user_data review_data = new Review_user_data();
                    try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                        String res = review_data.execute(IP_ADDRESS + "/review_user_data.php", user_id, snack_name, taste_score, cost_score, keyword_list.get(0), keyword_list.get(1), keyword_list.get(2), "0").get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        else { // 과거의 리뷰를 단 적이 있는 경우
            final String[] past_rate = past_data.split("#"); // 앞에서부터 순서대로 과거의 taste, cost, keyword1, keyword2, keyword3이 들어감
            EditTaste.setText(past_rate[0]);
            EditCost.setText(past_rate[1]);
            /*Editkeyword1.setText(past_rate[2]);
            if(past_rate.length >= 4) Editkeyword2.setText(past_rate[3]);
            if(past_rate.length >= 5) Editkeyword3.setText(past_rate[4]);*/

            System.out.println("past review yes");
            System.out.println(past_data);

            final ArrayList<String> past_key;
            past_key = new ArrayList<>();
            past_key.add(past_rate[2]);
            if(past_rate.length >= 4) past_key.add(past_rate[3]);
            if(past_rate.length >= 5) past_key.add(past_rate[4]);

            int cnt = 0;  // 과거에 체크했던 키워드 체크하는 부분
            if(past_key.contains("sweet")){
                cb1.setChecked(true);
                cnt ++;
            }
            if(past_key.contains("spicy")) {
                cb2.setChecked(true);
                cnt ++;
            }
            if(past_key.contains("sour")) {
                cb3.setChecked(true);
                cnt ++;
            }
            if(past_key.contains("bitter")) {
                cb4.setChecked(true);
                cnt ++;
            }

            if (cnt == 3) { // 과거에 체크한 것이 이미 최대량이면 더이상 새로운 것을 체크하지 못하도록
                if (!cb1.isChecked()) cb1.setEnabled(false);
                if (!cb2.isChecked()) cb2.setEnabled(false);
                if (!cb3.isChecked()) cb3.setEnabled(false);
                if (!cb4.isChecked()) cb4.setEnabled(false);
            } else {
                cb1.setEnabled(true);
                cb2.setEnabled(true);
                cb3.setEnabled(true);
                cb4.setEnabled(true);
            }

            button_upload.setText("Revise Review");

            button_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String taste_score = EditTaste.getText().toString();
                    String cost_score = EditCost.getText().toString();

                    double double_taste_score = Double.parseDouble(taste_score);
                    double double_cost_score = Double.parseDouble(cost_score);
                    double double_number_of_rate = Double.parseDouble(snack_number_of_rate);

                    String average_taste = String.format("%.2f", (Double.parseDouble(snack_taste) * double_number_of_rate + double_taste_score - Double.parseDouble(past_rate[0])) / double_number_of_rate);
                    String average_cost = String.format("%.2f", (Double.parseDouble(snack_cost) * double_number_of_rate + double_cost_score - Double.parseDouble(past_rate[1])) / double_number_of_rate);
                    // 이미 과거에 리뷰를 했던 것이므로 double_number_of_rate 은 1 증가하지 않고,
                    // 분자에선 새로운 값으로 바꾸는 것이므로 기존의 값을 빼줘야 한다

                    UpdateData task = new UpdateData();

                    try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                        String res = task.execute(IP_ADDRESS + "/update_score.php", snack_name, average_taste, average_cost, Double.toString(double_number_of_rate + 1)).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    /*keyword_list.add(Editkeyword1.getText().toString().toLowerCase());
                    keyword_list.add(Editkeyword2.getText().toString().toLowerCase());
                    keyword_list.add(Editkeyword3.getText().toString().toLowerCase());*/

                    String sweet_score = "0";
                    String spicy_score = "0";
                    String sour_score = "0";
                    String bitter_score = "0";

                    if (keyword_list.contains("sweet")) sweet_score = "1";
                    if (keyword_list.contains("spicy")) spicy_score = "1";
                    if (keyword_list.contains("sour")) sour_score = "1";
                    if (keyword_list.contains("bitter")) bitter_score = "1";

                    /*ArrayList<String> past_keyword_list;
                    past_keyword_list = new ArrayList<>();
                    past_keyword_list.add(past_rate[2]);
                    if(past_rate.length >= 4) past_keyword_list.add(past_rate[3]);
                    if(past_rate.length >= 5) past_keyword_list.add(past_rate[4]);*/

                    if (past_key.contains("sweet")) sweet_score = String.valueOf(Integer.parseInt(sweet_score) - 1);
                    if (past_key.contains("spicy")) spicy_score = String.valueOf(Integer.parseInt(spicy_score) - 1);
                    if (past_key.contains("sour")) sour_score = String.valueOf(Integer.parseInt(sour_score) - 1);
                    if (past_key.contains("bitter")) bitter_score = String.valueOf(Integer.parseInt(bitter_score) - 1);

                    UpdateKeyScore task_key = new UpdateKeyScore();

                    try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                        String res = task_key.execute(IP_ADDRESS + "/update_snack.php", snack_name, sweet_score, spicy_score, sour_score, bitter_score).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    while(keyword_list.size() < 3) {
                        keyword_list.add("-");
                    }

                    // 이제 유저 테이블에 새로운 값, 키워드를 저장
                    Review_user_data review_data = new Review_user_data();
                    System.out.println("Snack_review test");
                    for(int i=0; i<3; i++)
                        System.out.println(keyword_list.get(i));
                    System.out.println("Snack_review test");
                    try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                        String res = review_data.execute(IP_ADDRESS + "/review_user_data.php", user_id, snack_name, taste_score, cost_score, keyword_list.get(0), keyword_list.get(1), keyword_list.get(2), "1").get();
                        // have_reviewed는 항상 1이므로 그냥 1을 넣음
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            });
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        keyword_list = new ArrayList<>();

        cb1 = (CheckBox)findViewById(R.id.checkbox_review_key1);
        cb2 = (CheckBox)findViewById(R.id.checkbox_review_key2);
        cb3 = (CheckBox)findViewById(R.id.checkbox_review_key3);
        cb4 = (CheckBox)findViewById(R.id.checkbox_review_key4);

        count = 0;
        if (cb1.isChecked()) {
            count++;
            keyword_list.add(cb1.getText().toString().toLowerCase());
        }
        if (cb2.isChecked()) {
            count++;
            keyword_list.add(cb2.getText().toString().toLowerCase());
        }
        if (cb3.isChecked()) {
            count++;
            keyword_list.add(cb3.getText().toString().toLowerCase());
        }
        if (cb4.isChecked()) {
            count++;
            keyword_list.add(cb4.getText().toString().toLowerCase());
        }

        if (count == 3) {
            if (!cb1.isChecked()) cb1.setEnabled(false);
            if (!cb2.isChecked()) cb2.setEnabled(false);
            if (!cb3.isChecked()) cb3.setEnabled(false);
            if (!cb4.isChecked()) cb4.setEnabled(false);
        } else {
            cb1.setEnabled(true);
            cb2.setEnabled(true);
            cb3.setEnabled(true);
            cb4.setEnabled(true);
        }

        final Button button_upload = (Button)findViewById(R.id.button_upload_review);
        if (count > 0) {
            // 키워드를 1개 이상 선택하면 버튼 활성화
            button_upload.setEnabled(true);
        }
        else {
            button_upload.setEnabled(false);
        }

    }

    class UpdateData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Snack_Review.this,
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

            String name = (String)params[1];
            String taste = (String)params[2];
            String cost = (String)params[3];
            String number_of_rate = (String)params[4];

            String serverURL = (String)params[0];
            String postParameters = "name=" + name + "&taste=" + taste + "&cost=" + cost + "&number_of_rate=" + number_of_rate;

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

    class UpdateKeyScore extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Snack_Review.this,
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

            String name = (String)params[1];
            String sweet = (String) params[2];
            String spicy = (String)params[3];
            String sour = (String)params[4];
            String bitter = (String)params[5];

            String serverURL = (String)params[0];
            String postParameters = "name=" + name + "&sweet=" + sweet + "&spicy=" + spicy + "&sour=" + sour + "&bitter=" + bitter;

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

    class Review_user_data extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Snack_Review.this,
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
            String name = (String)params[2];
            String taste = (String)params[3];
            String cost = (String)params[4];
            String key1 = (String)params[5];
            String key2 = (String)params[6];
            String key3 = (String)params[7];
            String have_reviewed = (String)params[8];

            String serverURL = (String)params[0];
            String postParameters = "user_id=" + user_id + "&name=" + name + "&taste=" + taste + "&cost=" + cost + "&key1=" + key1 + "&key2=" + key2 + "&key3=" + key3 + "&have_reviewed=" + have_reviewed;

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
}
