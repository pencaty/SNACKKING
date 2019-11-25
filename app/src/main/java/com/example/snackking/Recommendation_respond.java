package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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

public class Recommendation_respond extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btn1;
    //private ImageButton btn2;
    private ImageButton btn3;
    private ImageButton btn4;

    private ArrayList<Snack_DataStructure> list;          // 데이터를 넣은 리스트변수
    private ArrayList<Snack_DataStructure> arraylist;
    private ListView listView;          // 검색을 보여줄 리스트변수
    private static String IP_ADDRESS = "http://snack.dothome.co.kr/";
    private static String TAG = "snack_arrange";
    public String mJsonString;
    private String user_id;
    private int index = 0;
    private int keyword_number = 1;

    private ArrayList<Chatroom_DataStructure> chat_list; // 다른 사람들의 request를 모은 리스트

    private Search_Adapter adapter;      // 리스트뷰에 연결할 아답터
    private TextView text_no_request;
    private TextView text_additional;
    private TextView text_comment;
    private LinearLayout linear_respond;
    private ListView lv_respond;
    private Button but_skip;

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    private ArrayList<String> keyword_list;

    private ArrayList<String> snack_respond_list; // 사람들의 답변 모음

    private Chatroom_DataStructure request;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // 다양한 유저들의 request를 보고 그 중 추천해줄 수 있는 것들은 추천해주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation_respond);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        this.InitializeView();
        this.SetListener();

        index = -1;

        settingList();

        if(chat_list == null || chat_list.size() == 0) { //Chatroom의 size가 0일 때
            text_no_request.setVisibility(View.VISIBLE);
            text_additional.setVisibility(View.GONE);
            linear_respond.setVisibility(View.GONE);
            text_comment.setVisibility(View.GONE);
            lv_respond.setVisibility(View.GONE);
            but_skip.setVisibility(View.GONE);
        }
        else {
            text_no_request.setVisibility(View.GONE);
            text_additional.setVisibility(View.VISIBLE);
            linear_respond.setVisibility(View.VISIBLE);
            text_comment.setVisibility(View.VISIBLE);
            lv_respond.setVisibility(View.VISIBLE);
            but_skip.setVisibility(View.VISIBLE);

            request = chat_list.get(0);
            do {
                index++;
                if(index == chat_list.size()) break;

                request = chat_list.get(index);

                Get_responses task = new Get_responses();

                try {
                    mJsonString = task.execute(IP_ADDRESS + "/get_individual_responses.php", request.getuser()).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showResult_individual();  // 다 끝나면 snack_respond_list에 참여한 사람의 id가 있음 (Recommendation_response_result에서와 들어간 값의 타입이 다름 주의)
            }while(snack_respond_list != null && snack_respond_list.contains(user_id));

            //listview로 search_keyword한 결과 보여줌 -> 하나 선택하면 그 과자의 이름과 user_id를 recommend_id에 추가, User_data에서 Review_num++

            if(index == chat_list.size()) {
                text_no_request.setVisibility(View.VISIBLE);
                text_additional.setVisibility(View.GONE);
                linear_respond.setVisibility(View.GONE);
                text_comment.setVisibility(View.GONE);
                lv_respond.setVisibility(View.GONE);
                but_skip.setVisibility(View.GONE);
            }
            else {
                String fir = request.getkey1();
                String sec = request.getkey2();
                String thi = request.getkey3();

                tv1.setText(fir);
                if (sec != null || !sec.equals(" ")) tv2.setText(sec);
                if (thi != null || !thi.equals(" ")) tv3.setText(thi);

                keyword_list = new ArrayList<>();
                keyword_list.add(fir.toLowerCase());
                if (sec != null || !sec.equals(" ")) keyword_list.add(sec.toLowerCase());
                if (thi != null || !thi.equals(" ")) keyword_list.add(thi.toLowerCase());

                keyword_number = 1;
                if (sec != null || !sec.equals(" ")) keyword_number++;
                if (thi != null || !thi.equals(" ")) keyword_number++;

                text_comment.setText(request.getcomment());

                listView = (ListView) findViewById(R.id.listview_keyword_respond);

                list = new ArrayList<>(); // 리스트를 생성한다.

                settingList_search(); // 검색에 사용할 데이터을 미리 저장한다.   --> 여기서 과자 이름 데이터를 넣어야함.

                arraylist = new ArrayList<Snack_DataStructure>(); // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
                arraylist.addAll(list);

                adapter = new Search_Adapter(list, this); // 리스트에 연동될 아답터를 생성한다.
                listView.setAdapter(adapter); // 리스트뷰에 아답터를 연결한다.

                search();

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // recommend_(request.getuser()) 에 user_id와 list.get(position).getSnack_name() 추가
                        // answer_to_request.php 실행 (request, user_id, snack)에 (request.getuser(), user_id, list.get(position).getSnack_name())

                        Insert_recommend task = new Insert_recommend();
                        try {
                            String res = task.execute(IP_ADDRESS + "/answer_to_request.php", request.getuser(), user_id, list.get(position).getSnack_name()).get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        do { // 여기서부터는 skip 버튼의 내용 그대로
                            index++;

                            if(index == chat_list.size()) break;

                            request = chat_list.get(index);

                            Get_responses tasks = new Get_responses();

                            try {
                                mJsonString = tasks.execute(IP_ADDRESS + "/get_individual_responses.php", request.getuser()).get();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            showResult_individual();  // 다 끝나면 snack_respond_list에 참여한 사람의 id가 있음 (Recommendation_response_result에서와 들어간 값의 타입이 다름 주의)
                        }while(snack_respond_list != null && snack_respond_list.contains(user_id));


                        //listview로 search_keyword한 결과 보여줌 -> 하나 선택하면 그 과자의 이름과 user_id를 recommend_id에 추가, User_data에서 Review_num++

                        if(index == chat_list.size()) {
                            text_no_request.setVisibility(View.VISIBLE);
                            text_additional.setVisibility(View.GONE);
                            linear_respond.setVisibility(View.GONE);
                            text_comment.setVisibility(View.GONE);
                            lv_respond.setVisibility(View.GONE);
                            but_skip.setVisibility(View.GONE);
                        }
                        else {
                            String fir = request.getkey1();
                            String sec = request.getkey2();
                            String thi = request.getkey3();

                            tv1.setText(fir);
                            if (sec != null || !sec.equals(" ")) tv2.setText(sec);
                            if (thi != null && !thi.equals(" ")) tv3.setText(thi);

                            keyword_list = new ArrayList<>();
                            keyword_list.add(fir.toLowerCase());
                            if (sec != null || !sec.equals(" ")) keyword_list.add(sec.toLowerCase());
                            if (thi != null && !thi.equals(" ")) keyword_list.add(thi.toLowerCase());

                            keyword_number = 1;
                            if (sec != null && !sec.equals(" ")) keyword_number++;
                            if (thi != null && !thi.equals(" ")) keyword_number++;

                            text_comment.setText(request.getcomment());

                            listView = (ListView) findViewById(R.id.listview_keyword_respond);

                            list = new ArrayList<>(); // 리스트를 생성한다.

                            settingList_search(); // 검색에 사용할 데이터을 미리 저장한다.   --> 여기서 과자 이름 데이터를 넣어야함.

                            arraylist = new ArrayList<Snack_DataStructure>(); // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
                            arraylist.addAll(list);

                            adapter = new Search_Adapter(list, getApplicationContext()); // 리스트에 연동될 아답터를 생성한다.
                            listView.setAdapter(adapter); // 리스트뷰에 아답터를 연결한다.

                            search();
                        }
                    }
                });
            }

        }

    }

    public void search() { // 검색을 수행하는 메소드

        list.clear();
        // 키워드 검색 --> keyword_list에 최대 3개 들어있음
        // 키워드를 하나의 list처럼 주고, 모든 과자 돌면서 각 키워드의 지분을 더한 후 ~% 이상이면 list에 add.
        int number_of_key_have;
        int total_num = 0;
        for(int i = 0;i < arraylist.size(); i++) {            // 리스트의 모든 데이터를 검색한다.
            number_of_key_have = 0;
            if(keyword_list.contains(arraylist.get(i).getSnack_keyword_1().toLowerCase())) number_of_key_have++;
            if(keyword_list.contains(arraylist.get(i).getSnack_keyword_2().toLowerCase())) number_of_key_have++;
            if(keyword_list.contains(arraylist.get(i).getSnack_keyword_3().toLowerCase())) number_of_key_have++;

            if(number_of_key_have == keyword_number){
                list.add(arraylist.get(i));    // 검색된 데이터를 리스트에 추가한다.
                total_num++;
            }
        }
        if(total_num == 0) {
            for(int i = 0; i < arraylist.size(); i++) {
                list.add(arraylist.get(i));
            }
        }

        /*if(list.size() == 0) no_result.setVisibility(View.VISIBLE);
        else no_result.setVisibility(View.GONE);*/

        adapter.notifyDataSetChanged(); // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
    }

    private void settingList_search(){ // 검색에 사용될 데이터를 리스트에 추가한다.

        GetData task = new GetData();

        try {
            mJsonString = task.execute(IP_ADDRESS + "/get_snack_data.php", "").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        showResult_search();
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Recommendation_respond.this,
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

    private void showResult_search(){

        String TAG_JSON="snack_json";
        String TAG_NAME = "name";
        String TAG_TASTE = "taste";
        String TAG_COST = "cost";
        String TAG_NUMBER_OF_RATE  = "number_of_rate";
        String TAG_KEYWORD1 = "keyword1";
        String TAG_KEYWORD2 = "keyword2";
        String TAG_KEYWORD3 = "keyword3";

        list.clear();

        if(mJsonString != null) {
            try {

                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    String name = item.getString(TAG_NAME);
                    String taste = item.getString(TAG_TASTE);
                    String cost = item.getString(TAG_COST);
                    String number_of_rate = item.getString(TAG_NUMBER_OF_RATE);
                    String keyword_1 = item.getString(TAG_KEYWORD1);
                    String keyword_2 = item.getString(TAG_KEYWORD2);
                    String keyword_3 = item.getString(TAG_KEYWORD3);

                    Snack_DataStructure snackdata = new Snack_DataStructure();

                    snackdata.setSnack_name(name);
                    snackdata.setSnack_taste(taste);
                    snackdata.setSnack_cost(cost);
                    snackdata.setSnack_number_of_rate(number_of_rate);
                    snackdata.setSnack_keyword_1(keyword_1);
                    snackdata.setSnack_keyword_2(keyword_2);
                    snackdata.setSnack_keyword_3(keyword_3);

                    list.add(snackdata);
                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
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

            progressDialog = ProgressDialog.show(Recommendation_respond.this,
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
        String TAG_KEYWORD1 = "key1";
        String TAG_KEYWORD2 = "key2";
        String TAG_KEYWORD3 = "key3";
        String TAG_COMMENT = "comment";

        if(mJsonString != null) {
            try {

                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                chat_list = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    String request_id = item.getString(TAG_REQUEST_ID);
                    String keyword_1 = item.getString(TAG_KEYWORD1);
                    String keyword_2 = item.getString(TAG_KEYWORD2);
                    String keyword_3 = item.getString(TAG_KEYWORD3);
                    String comment = item.getString(TAG_COMMENT);

                    Chatroom_DataStructure chat = new Chatroom_DataStructure();
                    chat.setuser(request_id);
                    chat.setkey1(keyword_1);
                    chat.setkey2(keyword_2);
                    chat.setkey3(keyword_3);
                    chat.setcomment(comment);

                    chat_list.add(chat);
                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }

    private class Get_responses extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Recommendation_respond.this,
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

    private void showResult_individual(){

        String TAG_JSON="snack_json";
        String TAG_USER = "user";

        if(mJsonString != null) {
            try {

                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                snack_respond_list = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject item = jsonArray.getJSONObject(i);

                    String respond_user = item.getString(TAG_USER);

                    snack_respond_list.add(respond_user); // 나중에 snack_datastructure로 바꾸면 snack_info로 연결할 수 있을듯?

                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult : ", e);
            }
        }
    }

    class Insert_recommend extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Recommendation_respond.this,
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

            String request = (String)params[1];
            String user = (String)params[2];
            String snack = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters = "request=" + request + "&user_id=" + user + "&snack=" + snack;

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

        text_no_request = findViewById(R.id.Text_no_request);
        text_additional = findViewById(R.id.Text_additional);
        linear_respond = findViewById(R.id.linear_key_request);

        text_comment = findViewById(R.id.Text_show_comment);
        lv_respond = findViewById(R.id.listview_keyword_respond);
        but_skip = findViewById(R.id.button_skip);

        tv1 = (TextView)findViewById(R.id.Text_first_key_request);
        tv2 = (TextView)findViewById(R.id.Text_second_key_request);
        tv3 = (TextView)findViewById(R.id.Text_third_key_request);
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
            case R.id.button_skip:

                do {
                    index++;
                    System.out.println(index);
                    System.out.println("index");
                    if(index == chat_list.size()) break;

                    request = chat_list.get(index);

                    Get_responses task = new Get_responses();

                    try {
                        mJsonString = task.execute(IP_ADDRESS + "/get_individual_responses.php", request.getuser()).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    showResult_individual();  // 다 끝나면 snack_respond_list에 참여한 사람의 id가 있음 (Recommendation_response_result에서와 들어간 값의 타입이 다름 주의)
                }while(snack_respond_list != null && snack_respond_list.contains(user_id));


                //listview로 search_keyword한 결과 보여줌 -> 하나 선택하면 그 과자의 이름과 user_id를 recommend_id에 추가, User_data에서 Review_num++

                if(index == chat_list.size()) {
                    text_no_request.setVisibility(View.VISIBLE);
                    text_additional.setVisibility(View.GONE);
                    linear_respond.setVisibility(View.GONE);
                    text_comment.setVisibility(View.GONE);
                    lv_respond.setVisibility(View.GONE);
                    but_skip.setVisibility(View.GONE);
                }
                else {
                    String fir = request.getkey1();
                    String sec = request.getkey2();
                    String thi = request.getkey3();

                    tv1.setText(fir);
                    if (sec != null) tv2.setText(sec);
                    if (thi != null) tv3.setText(thi);

                    keyword_list = new ArrayList<>();
                    keyword_list.add(fir.toLowerCase());
                    if (sec != null) keyword_list.add(sec.toLowerCase());
                    if (thi != null) keyword_list.add(thi.toLowerCase());

                    keyword_number = 1;
                    if (sec != null && !sec.equals(" ")) keyword_number++;
                    if (thi != null && !thi.equals(" ")) keyword_number++;

                    text_comment.setText(request.getcomment());

                    listView = (ListView) findViewById(R.id.listview_keyword_respond);

                    list = new ArrayList<>(); // 리스트를 생성한다.

                    settingList_search(); // 검색에 사용할 데이터을 미리 저장한다.   --> 여기서 과자 이름 데이터를 넣어야함.

                    arraylist = new ArrayList<Snack_DataStructure>(); // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
                    arraylist.addAll(list);

                    adapter = new Search_Adapter(list, this); // 리스트에 연동될 아답터를 생성한다.
                    listView.setAdapter(adapter); // 리스트뷰에 아답터를 연결한다.

                    search();
                }
        }
    }

    public void SetListener() {
        btn1.setOnClickListener(this);
        //btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        but_skip.setOnClickListener(this);
    }
}
