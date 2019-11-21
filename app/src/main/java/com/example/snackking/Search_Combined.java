package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Search_Combined extends AppCompatActivity  implements CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    private ArrayList<Snack_DataStructure> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private Search_Adapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<Snack_DataStructure> arraylist;

    private static String IP_ADDRESS = "http://snack.dothome.co.kr/";
    private static String TAG = "snack_arrange";
    public String mJsonString;
    private String user_id;

    //ImageButton btn1;
    ImageButton btn2;
    ImageButton btn3;
    ImageButton btn4;

    private int count = 0;
    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;
    private CheckBox cb4;
    private CheckBox cb5;
    private CheckBox cb6;
    private CheckBox cb7;
    private CheckBox cb8;

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    private ArrayList<String> keyword_list;
    private ArrayList<TextView> tv_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_combined);

        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.Search_listView);

        this.InitializeView();
        this.SetListener();

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        keyword_list = new ArrayList<>();
        tv_list = new ArrayList<>();

        cb1 = (CheckBox)findViewById(R.id.checkbox_key1);
        cb2 = (CheckBox)findViewById(R.id.checkbox_key2);
        cb3 = (CheckBox)findViewById(R.id.checkbox_key3);
        cb4 = (CheckBox)findViewById(R.id.checkbox_key4);
        cb5 = (CheckBox)findViewById(R.id.checkbox_key5);
        cb6 = (CheckBox)findViewById(R.id.checkbox_key6);
        cb7 = (CheckBox)findViewById(R.id.checkbox_key7);
        cb8 = (CheckBox)findViewById(R.id.checkbox_key8);

        tv1 = (TextView)findViewById(R.id.Text_first_key);
        tv2 = (TextView)findViewById(R.id.Text_second_key);
        tv3 = (TextView)findViewById(R.id.Text_third_key);
        tv_list.add(tv1);
        tv_list.add(tv2);
        tv_list.add(tv3);

        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);
        cb4.setOnCheckedChangeListener(this);
        cb5.setOnCheckedChangeListener(this);
        cb6.setOnCheckedChangeListener(this);
        cb7.setOnCheckedChangeListener(this);
        cb8.setOnCheckedChangeListener(this);

        Button button_search = (Button)findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0){
                    Toast toast = Toast.makeText(getApplicationContext(), "Select at least one keyword", Toast.LENGTH_SHORT);
                    int offSetX = 0;
                    int offSetY = 0;
                    toast.setGravity(Gravity.CENTER, offSetX, offSetY);
                    toast.show();
                }
                else {
                    while(keyword_list.size() < 3) {
                        keyword_list.add("-");
                    }
                    Intent intent = new Intent(getApplicationContext(), Search_keyword_result.class);
                    intent.putExtra(("number"), count);
                    intent.putExtra(("first"), keyword_list.get(0).toLowerCase());
                    intent.putExtra(("second"), keyword_list.get(1).toLowerCase());
                    intent.putExtra(("third"), keyword_list.get(2).toLowerCase());
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }
            }
        });


        list = new ArrayList<>(); // 리스트를 생성한다.

        settingList(); // 검색에 사용할 데이터을 미리 저장한다.   --> 여기서 과자 이름 데이터를 넣어야함.

        arraylist = new ArrayList<Snack_DataStructure>(); // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist.addAll(list);

        list.clear();

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
                String text = editSearch.getText().toString().toLowerCase();
                search(text);  // search 메소드를 호출한다.
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Snack_Info.class);
                intent.putExtra("user_id", user_id);
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

        keyword_list = new ArrayList<>();
        tv_list = new ArrayList<>();

        cb1 = (CheckBox)findViewById(R.id.checkbox_key1);
        cb2 = (CheckBox)findViewById(R.id.checkbox_key2);
        cb3 = (CheckBox)findViewById(R.id.checkbox_key3);
        cb4 = (CheckBox)findViewById(R.id.checkbox_key4);
        cb5 = (CheckBox)findViewById(R.id.checkbox_key5);
        cb6 = (CheckBox)findViewById(R.id.checkbox_key6);
        cb7 = (CheckBox)findViewById(R.id.checkbox_key7);
        cb8 = (CheckBox)findViewById(R.id.checkbox_key8);

        tv1 = (TextView)findViewById(R.id.Text_first_key);
        tv2 = (TextView)findViewById(R.id.Text_second_key);
        tv3 = (TextView)findViewById(R.id.Text_third_key);
        tv_list.add(tv1);
        tv_list.add(tv2);
        tv_list.add(tv3);

        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);
        cb4.setOnCheckedChangeListener(this);
        cb5.setOnCheckedChangeListener(this);
        cb6.setOnCheckedChangeListener(this);
        cb7.setOnCheckedChangeListener(this);
        cb8.setOnCheckedChangeListener(this);

        Button button_search = (Button)findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0){
                    Toast toast = Toast.makeText(getApplicationContext(), "Select at least one keyword", Toast.LENGTH_SHORT);
                    int offSetX = 0;
                    int offSetY = 0;
                    toast.setGravity(Gravity.CENTER, offSetX, offSetY);
                    toast.show();
                }
                else {
                    while(keyword_list.size() < 3) {
                        keyword_list.add("-");
                    }

                    Intent intent = new Intent(getApplicationContext(), Search_keyword_result.class);
                    intent.putExtra(("number"), count);
                    intent.putExtra(("first"), keyword_list.get(0).toLowerCase());
                    intent.putExtra(("second"), keyword_list.get(1).toLowerCase());
                    intent.putExtra(("third"), keyword_list.get(2).toLowerCase());
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }
            }
        });


        list = new ArrayList<>(); // 리스트를 생성한다.

        settingList(); // 검색에 사용할 데이터을 미리 저장한다.   --> 여기서 과자 이름 데이터를 넣어야함.

        arraylist = new ArrayList<Snack_DataStructure>(); // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist.addAll(list);

        list.clear();

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
                String text = editSearch.getText().toString().toLowerCase();
                search(text);  // search 메소드를 호출한다.
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Snack_Info.class);
                intent.putExtra("user_id", user_id);
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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        keyword_list = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            tv_list.get(i).setText("");
        }

        count = 0;
        if(cb1.isChecked()) {
            count++;
            keyword_list.add(cb1.getText().toString());
            tv_list.get(count-1).setText(cb1.getText().toString());
        }
        if(cb2.isChecked()) {
            count++;
            keyword_list.add(cb2.getText().toString());
            tv_list.get(count-1).setText(cb2.getText().toString());
        }
        if(cb3.isChecked()) {
            count++;
            keyword_list.add(cb3.getText().toString());
            tv_list.get(count-1).setText(cb3.getText().toString());
        }
        if(cb4.isChecked()) {
            count++;
            keyword_list.add(cb4.getText().toString());
            tv_list.get(count-1).setText(cb4.getText().toString());
        }
        if(cb5.isChecked()) {
            count++;
            keyword_list.add(cb5.getText().toString());
            tv_list.get(count-1).setText(cb5.getText().toString());
        }
        if(cb6.isChecked()) {
            count++;
            keyword_list.add(cb6.getText().toString());
            tv_list.get(count-1).setText(cb6.getText().toString());
        }
        if(cb7.isChecked()) {
            count++;
            keyword_list.add(cb7.getText().toString());
            tv_list.get(count-1).setText(cb7.getText().toString());
        }
        if(cb8.isChecked()) {
            count++;
            keyword_list.add(cb8.getText().toString());
            tv_list.get(count-1).setText(cb8.getText().toString());
        }

        if(count == 3){
            if(!cb1.isChecked()) cb1.setEnabled(false);
            if(!cb2.isChecked()) cb2.setEnabled(false);
            if(!cb3.isChecked()) cb3.setEnabled(false);
            if(!cb4.isChecked()) cb4.setEnabled(false);
            if(!cb5.isChecked()) cb5.setEnabled(false);
            if(!cb6.isChecked()) cb6.setEnabled(false);
            if(!cb7.isChecked()) cb7.setEnabled(false);
            if(!cb8.isChecked()) cb8.setEnabled(false);
        }
        else{
            cb1.setEnabled(true);
            cb2.setEnabled(true);
            cb3.setEnabled(true);
            cb4.setEnabled(true);
            cb5.setEnabled(true);
            cb6.setEnabled(true);
            cb7.setEnabled(true);
            cb8.setEnabled(true);
        }
    }

    public void search(String charText) { // 검색을 수행하는 메소드

        list.clear(); // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        if (charText.length() == 0) {         // 문자 입력이 없을때는 모든 데이터를 보여준다.
            //list.addAll(arraylist);

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
            mJsonString = task.execute(IP_ADDRESS + "/get_snack_data.php", "").get();
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

            progressDialog = ProgressDialog.show(Search_Combined.this,
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
                startActivity(intent);
                overridePendingTransition(0, 0);
                this.finish();
                break;
            case R.id.imageButton3:
                intent = new Intent(this, Achievement.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                this.finish();
                break;
            case R.id.imageButton4:
                intent = new Intent(this, Setting.class);
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