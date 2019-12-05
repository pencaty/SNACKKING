package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Search_Keyword_Result extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<Snack_DataStructure> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private Search_Adapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<Snack_DataStructure> arraylist;

    private String first_keyword;
    private String second_keyword;
    private String third_keyword;
    private int keyword_number = 0;
    private ArrayList<String> keyword_list;

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView no_result;

    //ImageButton btn1;
    ImageView btn2;
    ImageView btn3;
    ImageView btn4;

    private static String IP_ADDRESS = "http://snack.dothome.co.kr/";
    private static String TAG = "snack_arrange";
    public String mJsonString;

    private String user_id;

    private Switch switch_sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_keyword_result);

        this.InitializeView();
        this.SetListener();

        Intent intent = getIntent();
        keyword_number = intent.getIntExtra("number", 1);
        first_keyword = intent.getStringExtra("first");
        second_keyword = intent.getStringExtra("second");
        third_keyword = intent.getStringExtra("third");
        user_id = intent.getStringExtra("user_id");

        tv1 = (TextView)findViewById(R.id.Text_first_key_intent);
        tv2 = (TextView)findViewById(R.id.Text_second_key_intent);
        tv3 = (TextView)findViewById(R.id.Text_third_key_intent);
        no_result = (TextView)findViewById(R.id.Text_key_no_result);
        tv1.setText(first_keyword);
        if(second_keyword != null) tv2.setText(second_keyword);
        if(third_keyword != null) tv3.setText(third_keyword);

        keyword_list = new ArrayList<>();
        keyword_list.add(first_keyword.toLowerCase());
        if(second_keyword != null) keyword_list.add(second_keyword.toLowerCase());
        if(third_keyword != null) keyword_list.add(third_keyword.toLowerCase());

        listView = (ListView) findViewById(R.id.Search_listView_key);

        list = new ArrayList<>(); // 리스트를 생성한다.

        switch_sort =  (Switch) findViewById(R.id.switch_sort);

        settingList(); // 검색에 사용할 데이터을 미리 저장한다.   --> 여기서 과자 이름 데이터를 넣어야함.

        arraylist = new ArrayList<Snack_DataStructure>(); // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist.addAll(list);

        adapter = new Search_Adapter(list, this); // 리스트에 연동될 아답터를 생성한다.
        listView.setAdapter(adapter); // 리스트뷰에 아답터를 연결한다.

        search();

        switch_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!switch_sort.isChecked()) switch_sort.setText("Taste");
                else switch_sort.setText("Cost");
                settingList();

                arraylist = new ArrayList<Snack_DataStructure>(); // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
                arraylist.addAll(list);

                adapter = new Search_Adapter(list, getApplicationContext()); // 리스트에 연동될 아답터를 생성한다.
                listView.setAdapter(adapter); // 리스트뷰에 아답터를 연결한다.

                search();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Snack_Info.class);
                intent.putExtra(("name"), list.get(position).getSnack_name());
                intent.putExtra(("taste"), list.get(position).getSnack_taste());
                intent.putExtra(("cost"), list.get(position).getSnack_cost());
                intent.putExtra(("number"), list.get(position).getSnack_number_of_rate());
                intent.putExtra(("keyword_1"), list.get(position).getSnack_keyword_1());
                intent.putExtra(("keyword_2"), list.get(position).getSnack_keyword_2());
                intent.putExtra(("keyword_3"), list.get(position).getSnack_keyword_3());
                intent.putExtra(("user_id"), user_id);
                startActivity(intent);
            }
        });
    }

  public void search() { // 검색을 수행하는 메소드

        list.clear();
        // 키워드 검색 --> keyword_list에 최대 3개 들어있음
        // 키워드를 하나의 list처럼 주고, 모든 과자 돌면서 각 키워드의 지분을 더한 후 ~% 이상이면 list에 add.
        int number_of_key_have;
        for(int i = 0;i < arraylist.size(); i++) {            // 리스트의 모든 데이터를 검색한다.
            number_of_key_have = 0;
            if(keyword_list.contains(arraylist.get(i).getSnack_keyword_1().toLowerCase())) number_of_key_have++;
            if(keyword_list.contains(arraylist.get(i).getSnack_keyword_2().toLowerCase())) number_of_key_have++;
            if(keyword_list.contains(arraylist.get(i).getSnack_keyword_3().toLowerCase())) number_of_key_have++;

            if(number_of_key_have == keyword_number){
                list.add(arraylist.get(i));    // 검색된 데이터를 리스트에 추가한다.
            }
        }

        if(list.size() == 0) no_result.setVisibility(View.VISIBLE);
        else no_result.setVisibility(View.GONE);

        adapter.notifyDataSetChanged(); // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
    }

    private void settingList(){ // 검색에 사용될 데이터를 리스트에 추가한다.

        GetData task = new GetData();

        try {
            if(!switch_sort.isChecked()) mJsonString = task.execute(IP_ADDRESS + "/get_snack_order_by_taste.php", "").get();
            else mJsonString = task.execute(IP_ADDRESS + "/get_snack_order_by_cost.php", "").get();
            //mJsonString = task.execute(IP_ADDRESS + "/get_snack_data.php", "").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        showResult();
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Search_Keyword_Result.this,
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