package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Snack_Review extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private static String IP_ADDRESS = "http://snack.dothome.co.kr/";
    private static String TAG = "Insert_data";

    private String snack_name;
    private String snack_taste;
    private String snack_cost;
    private String snack_number_of_rate;

    private TextView Text_SnackName;
    private ImageView review_snack_image;

    private String have_reviewed;
    private String past_data;
    private String user_id;

    private int count = 0;
    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;
    private CheckBox cb4;
    private CheckBox cb5;
    private CheckBox cb6;
    private CheckBox cb7;
    private CheckBox cb8;

    //private ImageButton btn1;
    private ImageView btn2;
    private ImageView btn3;
    private ImageView btn4;

    private ImageButton btn_taste1;
    private ImageButton btn_taste2;
    private ImageButton btn_taste3;
    private ImageButton btn_taste4;
    private ImageButton btn_taste5;
    private ImageButton btn_cost1;
    private ImageButton btn_cost2;
    private ImageButton btn_cost3;;
    private ImageButton btn_cost4;
    private ImageButton btn_cost5;

    private String taste_score = "0"; // 기본값으로 4를 주자
    private String cost_score = "0";


    private ArrayList<String> keyword_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_review);

        this.InitializeView();
        this.SetListener();

        Intent intent = getIntent();

        Text_SnackName = (TextView)findViewById(R.id.review_snack_name);

        keyword_list = new ArrayList<>();

        cb1 = (CheckBox)findViewById(R.id.checkbox_review_key1);
        cb2 = (CheckBox)findViewById(R.id.checkbox_review_key2);
        cb3 = (CheckBox)findViewById(R.id.checkbox_review_key3);
        cb4 = (CheckBox)findViewById(R.id.checkbox_review_key4);
        cb5 = (CheckBox)findViewById(R.id.checkbox_review_key5);
        cb6 = (CheckBox)findViewById(R.id.checkbox_review_key6);
        cb7 = (CheckBox)findViewById(R.id.checkbox_review_key7);
        cb8 = (CheckBox)findViewById(R.id.checkbox_review_key8);

        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);
        cb4.setOnCheckedChangeListener(this);
        cb5.setOnCheckedChangeListener(this);
        cb6.setOnCheckedChangeListener(this);
        cb7.setOnCheckedChangeListener(this);
        cb8.setOnCheckedChangeListener(this);

        snack_name = intent.getStringExtra("name");
        snack_taste = intent.getStringExtra("taste");
        snack_cost = intent.getStringExtra("cost");
        snack_number_of_rate = intent.getStringExtra("number");

        have_reviewed = intent.getStringExtra("have_reviewed");
        past_data = intent.getStringExtra("past_data");
        user_id = intent.getStringExtra("user_id");

        Text_SnackName.setText(snack_name);

        // Change cookie icon to real image
        review_snack_image = (ImageView) findViewById(R.id.review_snack_image);

        Resources res = getResources();
        String name_snack = snack_name.replace(" ", "_").toLowerCase();
        if (name_snack == "karto") {
            name_snack = "karto";
        }
        int resID = res.getIdentifier(name_snack, "drawable", getPackageName());
        review_snack_image.setImageResource(resID);

        final Button button_upload = (Button)findViewById(R.id.button_upload_review);

        // 맨 처음에 버튼 비활성화 추가하기
        button_upload.setEnabled(false);

        if(have_reviewed.equals("0")) { // 과거에 리뷰를 단 적이 없는 경우
            button_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double double_taste_score = Double.parseDouble(taste_score);
                    double double_cost_score = Double.parseDouble(cost_score);
                    double double_number_of_rate = Double.parseDouble(snack_number_of_rate);

                    String average_taste = String.format("%.2f", (Double.parseDouble(snack_taste) * double_number_of_rate + double_taste_score) / (double_number_of_rate + 1));
                    String average_cost = String.format("%.2f", (Double.parseDouble(snack_cost) * double_number_of_rate + double_cost_score) / (double_number_of_rate + 1));

                    UpdateData task = new UpdateData();

                    try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                        String res = task.execute(IP_ADDRESS + "/update_score.php", snack_name, average_taste, average_cost, Double.toString(double_number_of_rate + 1)).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String sweet_score = "0";
                    String spicy_score = "0";
                    String bland_score = "0";
                    String nutty_score = "0";
                    String salty_score = "0";
                    String greasy_score = "0";
                    String crispy_score = "0";
                    String moist_score = "0";

                    if (keyword_list.contains("sweet")) sweet_score = "1";
                    if (keyword_list.contains("spicy")) spicy_score = "1";
                    if (keyword_list.contains("bland")) bland_score = "1";
                    if (keyword_list.contains("nutty")) nutty_score = "1";
                    if (keyword_list.contains("salty")) salty_score = "1";
                    if (keyword_list.contains("greasy")) greasy_score = "1";
                    if (keyword_list.contains("crispy")) crispy_score = "1";
                    if (keyword_list.contains("moisturized")) moist_score = "1";

                    UpdateKeyScore task_key = new UpdateKeyScore();

                    try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                        String res = task_key.execute(IP_ADDRESS + "/update_snack.php", snack_name, sweet_score, spicy_score, bland_score, nutty_score, salty_score, greasy_score, crispy_score, moist_score).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // keyword 별 개수 가장 많은 순으로 3개를 Keyword_One, Two, Three에 넣자.
                    // --> UpdateKeyScore 관련 php에서 모두 다 끝난 후 하는 것으로.

                    // 이제 유저 개개인 테이블에 과자, 점수, 키워드를 저장   .

                    while (keyword_list.size() < 3) {
                        keyword_list.add("-");
                    }

                    Review_user_data review_data = new Review_user_data();
                    try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                        String res = review_data.execute(IP_ADDRESS + "/review_user_data.php", user_id, snack_name, taste_score, cost_score, keyword_list.get(0), keyword_list.get(1), keyword_list.get(2), "0").get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast toast = Toast.makeText(getApplicationContext(), "The review is uploaded", Toast.LENGTH_SHORT);
                    int offSetX = 0;
                    int offSetY = 0;
                    toast.setGravity(Gravity.CENTER, offSetX, offSetY);
                    toast.show();
                }
            });
        }
        else { // 과거의 리뷰를 단 적이 있는 경우
            final String[] past_rate = past_data.split("#"); // 앞에서부터 순서대로 과거의 taste, cost, keyword1, keyword2, keyword3이 들어감

            switch (past_rate[0]) {
                case "1":
                    btn_taste1.setImageResource(R.drawable.yellow_star);
                    btn_taste2.setImageResource(R.drawable.basic_star);
                    btn_taste3.setImageResource(R.drawable.basic_star);
                    btn_taste4.setImageResource(R.drawable.basic_star);
                    btn_taste5.setImageResource(R.drawable.basic_star);
                    taste_score = "1";
                    break;
                case "2":
                    btn_taste1.setImageResource(R.drawable.yellow_star);
                    btn_taste2.setImageResource(R.drawable.yellow_star);
                    btn_taste3.setImageResource(R.drawable.basic_star);
                    btn_taste4.setImageResource(R.drawable.basic_star);
                    btn_taste5.setImageResource(R.drawable.basic_star);
                    taste_score = "2";
                    break;
                case "3":
                    btn_taste1.setImageResource(R.drawable.yellow_star);
                    btn_taste2.setImageResource(R.drawable.yellow_star);
                    btn_taste3.setImageResource(R.drawable.yellow_star);
                    btn_taste4.setImageResource(R.drawable.basic_star);
                    btn_taste5.setImageResource(R.drawable.basic_star);
                    taste_score = "3";
                    break;
                case "4":
                    btn_taste1.setImageResource(R.drawable.yellow_star);
                    btn_taste2.setImageResource(R.drawable.yellow_star);
                    btn_taste3.setImageResource(R.drawable.yellow_star);
                    btn_taste4.setImageResource(R.drawable.yellow_star);
                    btn_taste5.setImageResource(R.drawable.basic_star);
                    taste_score = "4";
                    break;
                case "5":
                    btn_taste1.setImageResource(R.drawable.yellow_star);
                    btn_taste2.setImageResource(R.drawable.yellow_star);
                    btn_taste3.setImageResource(R.drawable.yellow_star);
                    btn_taste4.setImageResource(R.drawable.yellow_star);
                    btn_taste5.setImageResource(R.drawable.yellow_star);
                    taste_score = "5";
                    break;
            }

            switch(past_rate[1]){
                case "1":
                    btn_cost1.setImageResource(R.drawable.yellow_star);
                    btn_cost2.setImageResource(R.drawable.basic_star);
                    btn_cost3.setImageResource(R.drawable.basic_star);
                    btn_cost4.setImageResource(R.drawable.basic_star);
                    btn_cost5.setImageResource(R.drawable.basic_star);
                    cost_score = "1";
                    break;
                case "2":
                    btn_cost1.setImageResource(R.drawable.yellow_star);
                    btn_cost2.setImageResource(R.drawable.yellow_star);
                    btn_cost3.setImageResource(R.drawable.basic_star);
                    btn_cost4.setImageResource(R.drawable.basic_star);
                    btn_cost5.setImageResource(R.drawable.basic_star);
                    cost_score = "2";
                    break;
                case "3":
                    btn_cost1.setImageResource(R.drawable.yellow_star);
                    btn_cost2.setImageResource(R.drawable.yellow_star);
                    btn_cost3.setImageResource(R.drawable.yellow_star);
                    btn_cost4.setImageResource(R.drawable.basic_star);
                    btn_cost5.setImageResource(R.drawable.basic_star);
                    cost_score = "3";
                    break;
                case "4":
                    btn_cost1.setImageResource(R.drawable.yellow_star);
                    btn_cost2.setImageResource(R.drawable.yellow_star);
                    btn_cost3.setImageResource(R.drawable.yellow_star);
                    btn_cost4.setImageResource(R.drawable.yellow_star);
                    btn_cost5.setImageResource(R.drawable.basic_star);
                    cost_score = "4";
                    break;
                case "5":
                    btn_cost1.setImageResource(R.drawable.yellow_star);
                    btn_cost2.setImageResource(R.drawable.yellow_star);
                    btn_cost3.setImageResource(R.drawable.yellow_star);
                    btn_cost4.setImageResource(R.drawable.yellow_star);
                    btn_cost5.setImageResource(R.drawable.yellow_star);
                    cost_score = "5";
                    break;
            }

            final ArrayList<String> past_key;
            past_key = new ArrayList<>();
            past_key.add(past_rate[2]);
            if(past_rate.length >= 4) past_key.add(past_rate[3]);
            if(past_rate.length >= 5) past_key.add(past_rate[4]);

            count = 0;  // 과거에 체크했던 키워드 체크하는 부분
            if(past_key.contains("sweet")){
                cb1.setChecked(true);
                count ++;
            }
            if(past_key.contains("spicy")) {
                cb2.setChecked(true);
                count ++;
            }
            if(past_key.contains("bland")) {
                cb3.setChecked(true);
                count ++;
            }
            if(past_key.contains("nutty")) {
                cb4.setChecked(true);
                count ++;
            }
            if(past_key.contains("salty")){
                cb5.setChecked(true);
                count ++;
            }
            if(past_key.contains("greasy")) {
                cb6.setChecked(true);
                count ++;
            }
            if(past_key.contains("crispy")) {
                cb7.setChecked(true);
                count ++;
            }
            if(past_key.contains("moisturized")) {
                cb8.setChecked(true);
                count ++;
            }

            if (count == 3) { // 과거에 체크한 것이 이미 최대량이면 더이상 새로운 것을 체크하지 못하도록
                if (!cb1.isChecked()) cb1.setEnabled(false);
                if (!cb2.isChecked()) cb2.setEnabled(false);
                if (!cb3.isChecked()) cb3.setEnabled(false);
                if (!cb4.isChecked()) cb4.setEnabled(false);
                if (!cb5.isChecked()) cb5.setEnabled(false);
                if (!cb6.isChecked()) cb6.setEnabled(false);
                if (!cb7.isChecked()) cb7.setEnabled(false);
                if (!cb8.isChecked()) cb8.setEnabled(false);
            } else {
                cb1.setEnabled(true);
                cb2.setEnabled(true);
                cb3.setEnabled(true);
                cb4.setEnabled(true);
                cb5.setEnabled(true);
                cb6.setEnabled(true);
                cb7.setEnabled(true);
                cb8.setEnabled(true);
            }

            button_upload.setText("Revise Review");

            button_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double double_taste_score = Double.parseDouble(taste_score);
                    double double_cost_score = Double.parseDouble(cost_score);
                    double double_number_of_rate = Double.parseDouble(snack_number_of_rate);

                    String average_taste = String.format("%.2f", (Double.parseDouble(snack_taste) * double_number_of_rate + double_taste_score - Double.parseDouble(past_rate[0])) / double_number_of_rate);
                    String average_cost = String.format("%.2f", (Double.parseDouble(snack_cost) * double_number_of_rate + double_cost_score - Double.parseDouble(past_rate[1])) / double_number_of_rate);
                    // 이미 과거에 리뷰를 했던 것이므로 double_number_of_rate 은 1 증가하지 않고,
                    // 분자에선 새로운 값으로 바꾸는 것이므로 기존의 값을 빼줘야 한다

                    UpdateData task = new UpdateData();

                    try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                        String res = task.execute(IP_ADDRESS + "/update_score.php", snack_name, average_taste, average_cost, Double.toString(double_number_of_rate)).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String sweet_score = "0";
                    String spicy_score = "0";
                    String bland_score = "0";
                    String nutty_score = "0";
                    String salty_score = "0";
                    String greasy_score = "0";
                    String crispy_score = "0";
                    String moist_score = "0";

                    if (keyword_list.contains("sweet")) sweet_score = "1";
                    if (keyword_list.contains("spicy")) spicy_score = "1";
                    if (keyword_list.contains("bland")) bland_score = "1";
                    if (keyword_list.contains("nutty")) nutty_score = "1";
                    if (keyword_list.contains("salty")) salty_score = "1";
                    if (keyword_list.contains("greasy")) greasy_score = "1";
                    if (keyword_list.contains("crispy")) crispy_score = "1";
                    if (keyword_list.contains("moisturized")) moist_score = "1";


                    if (past_key.contains("sweet"))
                        sweet_score = String.valueOf(Integer.parseInt(sweet_score) - 1);
                    if (past_key.contains("spicy"))
                        spicy_score = String.valueOf(Integer.parseInt(spicy_score) - 1);
                    if (past_key.contains("bland"))
                        bland_score = String.valueOf(Integer.parseInt(bland_score) - 1);
                    if (past_key.contains("nutty"))
                        nutty_score = String.valueOf(Integer.parseInt(nutty_score) - 1);
                    if (past_key.contains("salty"))
                        salty_score = String.valueOf(Integer.parseInt(salty_score) - 1);
                    if (past_key.contains("greasy"))
                        greasy_score = String.valueOf(Integer.parseInt(greasy_score) - 1);
                    if (past_key.contains("crispy"))
                        crispy_score = String.valueOf(Integer.parseInt(crispy_score) - 1);
                    if (past_key.contains("moisturized"))
                        moist_score = String.valueOf(Integer.parseInt(moist_score) - 1);

                    UpdateKeyScore task_key = new UpdateKeyScore();

                    try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                        String res = task_key.execute(IP_ADDRESS + "/update_snack.php", snack_name, sweet_score, spicy_score, bland_score, nutty_score, salty_score, greasy_score, crispy_score, moist_score).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    while (keyword_list.size() < 3) {
                        keyword_list.add("-");
                    }

                    // 이제 유저 테이블에 새로운 값, 키워드를 저장
                    Review_user_data review_data = new Review_user_data();

                    try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                        String res = review_data.execute(IP_ADDRESS + "/review_user_data.php", user_id, snack_name, taste_score, cost_score, keyword_list.get(0), keyword_list.get(1), keyword_list.get(2), "1").get();
                        // have_reviewed는 항상 1이므로 그냥 1을 넣음
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast toast = Toast.makeText(getApplicationContext(), "The review is revised", Toast.LENGTH_SHORT);
                    int offSetX = 0;
                    int offSetY = 0;
                    toast.setGravity(Gravity.CENTER, offSetX, offSetY);
                    toast.show();
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
        cb5 = (CheckBox)findViewById(R.id.checkbox_review_key5);
        cb6 = (CheckBox)findViewById(R.id.checkbox_review_key6);
        cb7 = (CheckBox)findViewById(R.id.checkbox_review_key7);
        cb8 = (CheckBox)findViewById(R.id.checkbox_review_key8);

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
        if (cb5.isChecked()) {
            count++;
            keyword_list.add(cb5.getText().toString().toLowerCase());
        }
        if (cb6.isChecked()) {
            count++;
            keyword_list.add(cb6.getText().toString().toLowerCase());
        }
        if (cb7.isChecked()) {
            count++;
            keyword_list.add(cb7.getText().toString().toLowerCase());
        }
        if (cb8.isChecked()) {
            count++;
            keyword_list.add(cb8.getText().toString().toLowerCase());
        }

        if (count == 3) { // 과거에 체크한 것이 이미 최대량이면 더이상 새로운 것을 체크하지 못하도록
            if (!cb1.isChecked()) cb1.setEnabled(false);
            if (!cb2.isChecked()) cb2.setEnabled(false);
            if (!cb3.isChecked()) cb3.setEnabled(false);
            if (!cb4.isChecked()) cb4.setEnabled(false);
            if (!cb5.isChecked()) cb5.setEnabled(false);
            if (!cb6.isChecked()) cb6.setEnabled(false);
            if (!cb7.isChecked()) cb7.setEnabled(false);
            if (!cb8.isChecked()) cb8.setEnabled(false);
        } else {
            cb1.setEnabled(true);
            cb2.setEnabled(true);
            cb3.setEnabled(true);
            cb4.setEnabled(true);
            cb5.setEnabled(true);
            cb6.setEnabled(true);
            cb7.setEnabled(true);
            cb8.setEnabled(true);
        }

        ButtonActivate();

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
            String bland = (String)params[4];
            String nutty = (String)params[5];
            String salty = (String) params[6];
            String greasy = (String)params[7];
            String crispy = (String)params[8];
            String moist = (String)params[9];

            String serverURL = (String)params[0];
            String postParameters = "name=" + name + "&sweet=" + sweet + "&spicy=" + spicy + "&bland=" + bland + "&nutty=" + nutty + "&salty=" + salty + "&greasy=" + greasy + "&crispy=" + crispy + "&moist=" + moist;

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
            //snack_name, taste_score, cost_score, sweet_score, spicy_score, bland_score, nutty_score).get();
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

    public void InitializeView() {
        //btn1 = findViewById(R.id.imageButton);
        btn2 = findViewById(R.id.imageButton2);
        btn3 = findViewById(R.id.imageButton3);
        btn4 = findViewById(R.id.imageButton4);

        btn_taste1 = findViewById(R.id.button_taste1);
        btn_taste2 = findViewById(R.id.button_taste2);
        btn_taste3 = findViewById(R.id.button_taste3);
        btn_taste4 = findViewById(R.id.button_taste4);
        btn_taste5 = findViewById(R.id.button_taste5);
        btn_cost1 = findViewById(R.id.button_cost1);
        btn_cost2 = findViewById(R.id.button_cost2);
        btn_cost3 = findViewById(R.id.button_cost3);
        btn_cost4 = findViewById(R.id.button_cost4);
        btn_cost5 = findViewById(R.id.button_cost5);
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

            case R.id.button_taste1:
                btn_taste1.setImageResource(R.drawable.yellow_star);
                btn_taste2.setImageResource(R.drawable.basic_star);
                btn_taste3.setImageResource(R.drawable.basic_star);
                btn_taste4.setImageResource(R.drawable.basic_star);
                btn_taste5.setImageResource(R.drawable.basic_star);
                taste_score = "1";
                ButtonActivate();
                break;
            case R.id.button_taste2:
                btn_taste1.setImageResource(R.drawable.yellow_star);
                btn_taste2.setImageResource(R.drawable.yellow_star);
                btn_taste3.setImageResource(R.drawable.basic_star);
                btn_taste4.setImageResource(R.drawable.basic_star);
                btn_taste5.setImageResource(R.drawable.basic_star);
                taste_score = "2";
                ButtonActivate();
                break;
            case R.id.button_taste3:
                btn_taste1.setImageResource(R.drawable.yellow_star);
                btn_taste2.setImageResource(R.drawable.yellow_star);
                btn_taste3.setImageResource(R.drawable.yellow_star);
                btn_taste4.setImageResource(R.drawable.basic_star);
                btn_taste5.setImageResource(R.drawable.basic_star);
                taste_score = "3";
                ButtonActivate();
                break;
            case R.id.button_taste4:
                btn_taste1.setImageResource(R.drawable.yellow_star);
                btn_taste2.setImageResource(R.drawable.yellow_star);
                btn_taste3.setImageResource(R.drawable.yellow_star);
                btn_taste4.setImageResource(R.drawable.yellow_star);
                btn_taste5.setImageResource(R.drawable.basic_star);
                taste_score = "4";
                ButtonActivate();
                break;
            case R.id.button_taste5:
                btn_taste1.setImageResource(R.drawable.yellow_star);
                btn_taste2.setImageResource(R.drawable.yellow_star);
                btn_taste3.setImageResource(R.drawable.yellow_star);
                btn_taste4.setImageResource(R.drawable.yellow_star);
                btn_taste5.setImageResource(R.drawable.yellow_star);
                taste_score = "5";
                ButtonActivate();
                break;
            case R.id.button_cost1:
                btn_cost1.setImageResource(R.drawable.yellow_star);
                btn_cost2.setImageResource(R.drawable.basic_star);
                btn_cost3.setImageResource(R.drawable.basic_star);
                btn_cost4.setImageResource(R.drawable.basic_star);
                btn_cost5.setImageResource(R.drawable.basic_star);
                cost_score = "1";
                ButtonActivate();
                break;
            case R.id.button_cost2:
                btn_cost1.setImageResource(R.drawable.yellow_star);
                btn_cost2.setImageResource(R.drawable.yellow_star);
                btn_cost3.setImageResource(R.drawable.basic_star);
                btn_cost4.setImageResource(R.drawable.basic_star);
                btn_cost5.setImageResource(R.drawable.basic_star);
                cost_score = "2";
                ButtonActivate();
                break;
            case R.id.button_cost3:
                btn_cost1.setImageResource(R.drawable.yellow_star);
                btn_cost2.setImageResource(R.drawable.yellow_star);
                btn_cost3.setImageResource(R.drawable.yellow_star);
                btn_cost4.setImageResource(R.drawable.basic_star);
                btn_cost5.setImageResource(R.drawable.basic_star);
                cost_score = "3";
                ButtonActivate();
                break;
            case R.id.button_cost4:
                btn_cost1.setImageResource(R.drawable.yellow_star);
                btn_cost2.setImageResource(R.drawable.yellow_star);
                btn_cost3.setImageResource(R.drawable.yellow_star);
                btn_cost4.setImageResource(R.drawable.yellow_star);
                btn_cost5.setImageResource(R.drawable.basic_star);
                cost_score = "4";
                ButtonActivate();
                break;
            case R.id.button_cost5:
                btn_cost1.setImageResource(R.drawable.yellow_star);
                btn_cost2.setImageResource(R.drawable.yellow_star);
                btn_cost3.setImageResource(R.drawable.yellow_star);
                btn_cost4.setImageResource(R.drawable.yellow_star);
                btn_cost5.setImageResource(R.drawable.yellow_star);
                cost_score = "5";
                ButtonActivate();
                break;
        }
    }

    public void SetListener() {
        //btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn_taste1.setOnClickListener(this);
        btn_taste2.setOnClickListener(this);
        btn_taste3.setOnClickListener(this);
        btn_taste4.setOnClickListener(this);
        btn_taste5.setOnClickListener(this);
        btn_cost1.setOnClickListener(this);
        btn_cost2.setOnClickListener(this);
        btn_cost3.setOnClickListener(this);
        btn_cost4.setOnClickListener(this);
        btn_cost5.setOnClickListener(this);
    }

    public void ButtonActivate() {
        final Button button_upload = (Button)findViewById(R.id.button_upload_review);
        if (count > 0 && !taste_score.equals("0") && !cost_score.equals("0")) {
            // 키워드를 1개 이상 선택하면 버튼 활성화
            button_upload.setEnabled(true);
        }
        else {
            button_upload.setEnabled(false);
        }
    }
}
