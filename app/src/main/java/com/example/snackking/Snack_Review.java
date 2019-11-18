package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Snack_Review extends AppCompatActivity {

    private static String IP_ADDRESS = "http://snack.dothome.co.kr/";
    private static String TAG = "Insert_data";

    private String snack_name;
    private String snack_taste;
    private String snack_cost;
    private String snack_number_of_rate;

    private EditText EditTaste;
    private EditText EditCost;
    private TextView Text_SnackName;

    private EditText Editkeyword1;
    private EditText Editkeyword2;
    private EditText Editkeyword3;

    private ArrayList<String> keyword_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_review);

        Intent intent = getIntent();

        Text_SnackName = (TextView)findViewById(R.id.review_snack_name);
        EditTaste = (EditText)findViewById(R.id.review_taste);
        EditCost = (EditText)findViewById(R.id.review_cost);

        Editkeyword1 = (EditText)findViewById((R.id.review_keyword1));
        Editkeyword2 = (EditText)findViewById((R.id.review_keyword2));
        Editkeyword3 = (EditText)findViewById((R.id.review_keyword3));

        keyword_list = new ArrayList<>();

        snack_name = intent.getStringExtra("name");
        snack_taste = intent.getStringExtra("taste");
        snack_cost = intent.getStringExtra("cost");
        snack_number_of_rate = intent.getStringExtra("number");

        Text_SnackName.setText(snack_name);

        final Button button_upload = (Button)findViewById(R.id.button_upload_review);

        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String taste_score = EditTaste.getText().toString();
                String cost_score = EditCost.getText().toString();

                double double_taste_score = Double.parseDouble(taste_score);
                double double_cost_score = Double.parseDouble(cost_score);
                double double_number_of_rate = Double.parseDouble(snack_number_of_rate);

                String average_taste = String.format("%.2f", (Double.parseDouble(snack_taste) * double_number_of_rate + double_taste_score) / (double_number_of_rate + 1));
                String average_cost = String.format("%.2f", (Double.parseDouble(snack_cost) * double_number_of_rate + double_cost_score) / (double_number_of_rate + 1));

                UpdateData task = new UpdateData();

                try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                    String res = task.execute(IP_ADDRESS + "/updatedata.php", snack_name, average_taste, average_cost, Double.toString(double_number_of_rate + 1)).get();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

                keyword_list.add(Editkeyword1.getText().toString().toLowerCase());
                keyword_list.add(Editkeyword2.getText().toString().toLowerCase());
                keyword_list.add(Editkeyword3.getText().toString().toLowerCase());

                String sweet_score = "0";
                String spicy_score = "0";
                String sour_score = "0";
                String bitter_score = "0";

                if(keyword_list.contains("sweet")) sweet_score = "1";
                if(keyword_list.contains("spicy")) spicy_score = "1";
                if(keyword_list.contains("sour")) sour_score = "1";
                if(keyword_list.contains("bitter")) bitter_score = "1";

                UpdateKey task_key = new UpdateKey();

                try { // 데이터베이스에 업데이트를 끝날 때까지 기다리려고
                    String res = task_key.execute(IP_ADDRESS + "/update_snack.php", snack_name, sweet_score, spicy_score, sour_score, bitter_score).get();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

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

    class UpdateKey extends AsyncTask<String, Void, String> {
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
}
