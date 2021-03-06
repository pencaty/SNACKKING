package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {

    private static String IP_ADDRESS = "http://snack.dothome.co.kr/";
    private static String TAG = "Insert_data";

    private EditText mEditText_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditText_login = (EditText)findViewById(R.id.editText_login_id);

        Button button_login = (Button)findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_id = mEditText_login.getText().toString();

                if(user_id.length() < 4) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please enter at least 4 letters", Toast.LENGTH_SHORT);
                    int offSetX = 0;
                    int offSetY = 0;
                    toast.setGravity(Gravity.CENTER, offSetX, offSetY);
                    toast.show();
                }
                else {
                    Insert_User_Data task = new Insert_User_Data();
                    try {
                        String res = task.execute(IP_ADDRESS + "/insert_user_data.php", user_id).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Intent intent = new Intent(getApplicationContext(), DB_snack_get.class);
                    Intent intent = new Intent(getApplicationContext(), Search_Combined.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);

                    // Login 버튼을 눌러서 다음 페이지로 갈 때
                    // 이미 있는 유저면 그 아이디를 가지고 디비 데이터 가져오기 가능
                    // 신규유저면 그 아이디로 새로 테이블을 만든다
                }
            }
        });
    }

    class Insert_User_Data extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Login.this,
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

            String user = (String)params[1];

            String serverURL = (String)params[0];
            String postParameters = "user=" + user;

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
