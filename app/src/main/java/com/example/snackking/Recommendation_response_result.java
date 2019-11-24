package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;

public class Recommendation_response_result extends AppCompatActivity implements View.OnClickListener {

    ImageButton btn1;
    //ImageButton btn2;
    ImageButton btn3;
    ImageButton btn4;

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) { // recommend_user_Id table을 가져와서 안에 있는 데이터를 전부 보여주기
        super.onCreate(savedInstanceState);              // + accept 버튼 누르면 recommend_user_table 삭제하고, chatroom에서 user_Id 있는 줄 삭제
        setContentView(R.layout.activity_recommendation_response_result);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        this.InitializeView();
        this.SetListener();
    }

    public void InitializeView() {
        btn1 = findViewById(R.id.imageButton1);
        //btn2 = findViewById(R.id.imageButton2);
        btn3 = findViewById(R.id.imageButton4);
        btn4 = findViewById(R.id.imageButton3);

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
        }
    }

    public void SetListener() {
        btn1.setOnClickListener(this);
        //btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

    }
}
