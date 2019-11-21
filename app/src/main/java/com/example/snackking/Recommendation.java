package com.example.snackking;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

public class Recommendation extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    ImageButton btn1;
    //ImageButton btn2;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        this.InitializeView();
        this.SetListener();

        Button button_recommend = (Button)findViewById(R.id.button_recommend);
        button_recommend.setOnClickListener(new View.OnClickListener() {
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
                    /*while(keyword_list.size() < 3) {
                        keyword_list.add("-");
                    }
                    Intent intent = new Intent(getApplicationContext(), Search_keyword_result.class);
                    intent.putExtra(("number"), count);
                    intent.putExtra(("first"), keyword_list.get(0).toLowerCase());
                    intent.putExtra(("second"), keyword_list.get(1).toLowerCase());
                    intent.putExtra(("third"), keyword_list.get(2).toLowerCase());
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);*/
                }
            }
        });

    }

    public void InitializeView() {
        btn1 = findViewById(R.id.imageButton1);
        //btn2 = findViewById(R.id.imageButton2);
        btn3 = findViewById(R.id.imageButton4);
        btn4 = findViewById(R.id.imageButton3);

        cb1 = (CheckBox)findViewById(R.id.checkbox_recom_key1);
        cb2 = (CheckBox)findViewById(R.id.checkbox_recom_key2);
        cb3 = (CheckBox)findViewById(R.id.checkbox_recom_key3);
        cb4 = (CheckBox)findViewById(R.id.checkbox_recom_key4);
        cb5 = (CheckBox)findViewById(R.id.checkbox_recom_key5);
        cb6 = (CheckBox)findViewById(R.id.checkbox_recom_key6);
        cb7 = (CheckBox)findViewById(R.id.checkbox_recom_key7);
        cb8 = (CheckBox)findViewById(R.id.checkbox_recom_key8);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButton1:
                Intent intent = new Intent(this, Search_Combined.class);
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
        btn1.setOnClickListener(this);
        //btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        cb1.setOnCheckedChangeListener(this);
        cb2.setOnCheckedChangeListener(this);
        cb3.setOnCheckedChangeListener(this);
        cb4.setOnCheckedChangeListener(this);
        cb5.setOnCheckedChangeListener(this);
        cb6.setOnCheckedChangeListener(this);
        cb7.setOnCheckedChangeListener(this);
        cb8.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

//        keyword_list = new ArrayList<>();

        count = 0;
        if(cb1.isChecked()) {
            count++;
//            keyword_list.add(cb1.getText().toString());
        }
        if(cb2.isChecked()) {
            count++;
//            keyword_list.add(cb2.getText().toString());
        }
        if(cb3.isChecked()) {
            count++;
//            keyword_list.add(cb3.getText().toString());
        }
        if(cb4.isChecked()) {
            count++;
//            keyword_list.add(cb4.getText().toString());
        }
        if(cb5.isChecked()) {
            count++;
//            keyword_list.add(cb5.getText().toString());
        }
        if(cb6.isChecked()) {
            count++;
//            keyword_list.add(cb6.getText().toString());
        }
        if(cb7.isChecked()) {
            count++;
//            keyword_list.add(cb7.getText().toString());
        }
        if(cb8.isChecked()) {
            count++;
//            keyword_list.add(cb8.getText().toString());
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

}
