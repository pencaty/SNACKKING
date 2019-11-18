package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Search_Snack_Data_from_DB extends AppCompatActivity {

    private ArrayList<Snack_DataStructure> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private Search_Adapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<Snack_DataStructure> arraylist;

    private static String IP_ADDRESS = "http://snack.dothome.co.kr/";
    private static String TAG = "snack_arrange";
    public String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.Search_listView);

        list = new ArrayList<>(); // 리스트를 생성한다.

        settingList(); // 검색에 사용할 데이터을 미리 저장한다.   --> 여기서 과자 이름 데이터를 넣어야함.

        arraylist = new ArrayList<Snack_DataStructure>(); // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist.addAll(list);

        adapter = new Search_Adapter(list, this); // 리스트에 연동될 아답터를 생성한다.
        listView.setAdapter(adapter); // 리스트뷰에 아답터를 연결한다.

        editSearch.addTextChangedListener(new TextWatcher() { // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) { // input창에 문자를 입력할때마다 호출된다.
                String text = editSearch.getText().toString();
                search(text);  // search 메소드를 호출한다.
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
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.Search_listView);

        list = new ArrayList<>(); // 리스트를 생성한다.

        settingList(); // 검색에 사용할 데이터을 미리 저장한다.   --> 여기서 과자 이름 데이터를 넣어야함.

        arraylist = new ArrayList<Snack_DataStructure>(); // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist.addAll(list);

        adapter = new Search_Adapter(list, this); // 리스트에 연동될 아답터를 생성한다.
        listView.setAdapter(adapter); // 리스트뷰에 아답터를 연결한다.

        editSearch.addTextChangedListener(new TextWatcher() { // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) { // input창에 문자를 입력할때마다 호출된다.
                String text = editSearch.getText().toString();
                search(text);  // search 메소드를 호출한다.
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
                startActivity(intent);
            }
        });
    }

    public void search(String charText) { // 검색을 수행하는 메소드

        list.clear(); // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        if (charText.length() == 0) {         // 문자 입력이 없을때는 모든 데이터를 보여준다.
            list.addAll(arraylist);

            // 키워드 검색
            // 키워드를 하나의 list처럼 주고, 모든 과자 돌면서 각 키워드의 지분을 더한 후 ~% 이상이면 list에 add.
            /*for(int i = 0;i < arraylist.size(); i++) {            // 리스트의 모든 데이터를 검색한다.
                if (arraylist.get(i).getSnack_keyword_1().toLowerCase().contains(charText)) {
                    list.add(arraylist.get(i));                    // 검색된 데이터를 리스트에 추가한다.
                }
            }
            for(int i = 0;i < arraylist.size(); i++) {            // 리스트의 모든 데이터를 검색한다.
                if (arraylist.get(i).getSnack_keyword_2().toLowerCase().contains(charText)) {
                    if(!list.contains(i))list.add(arraylist.get(i));                    // 검색된 데이터를 리스트에 추가한다.
                }
            }
            for(int i = 0;i < arraylist.size(); i++) {            // 리스트의 모든 데이터를 검색한다.
                if (arraylist.get(i).getSnack_keyword_3().toLowerCase().contains(charText)) {
                    if(!list.contains(i))list.add(arraylist.get(i));                    // 검색된 데이터를 리스트에 추가한다.
                }
            }*/
        }
        else { // 문자 입력을 할때..
            for(int i = 0;i < arraylist.size(); i++) {            // 리스트의 모든 데이터를 검색한다.
                if (arraylist.get(i).getSnack_name().toLowerCase().contains(charText)) {              // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                    list.add(arraylist.get(i));                    // 검색된 데이터를 리스트에 추가한다.
                }
            }
        }
        adapter.notifyDataSetChanged(); // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
    }

    private void settingList(){ // 검색에 사용될 데이터를 리스트에 추가한다.

        GetData task = new GetData();

        try {
            mJsonString = task.execute(IP_ADDRESS + "/getdata.php", "").get();
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

            progressDialog = ProgressDialog.show(Search_Snack_Data_from_DB.this,
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


        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

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