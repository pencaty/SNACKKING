package com.example.snackking;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;

public class Achievement extends AppCompatActivity implements View.OnClickListener {

    ImageButton btn1;
    ImageButton btn2;
    //ImageButton btn3;
    ImageButton btn4;

    Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        sw =  (Switch) findViewById(R.id.switch1);

        CheckState();

        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckState();
            }
        });

        this.InitializeView();
        this.SetListener();
    }

    public void InitializeView() {
        btn1 = findViewById(R.id.imageButton1);
        btn2 = findViewById(R.id.imageButton2);
        //btn3 = findViewById(R.id.imageButton4);
        btn4 = findViewById(R.id.imageButton4);
    }

    public void onClick(View view) {
        CheckState();
        switch (view.getId()) {
            case R.id.imageButton1:
                Intent intent = new Intent(this, Search_Combined.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                this.finish();
                break;
            case R.id.imageButton2:
                intent = new Intent(this, Recommendation.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                this.finish();
                break;
                /*
            case R.id.imageButton3:
                intent = new Intent(this, Achievement.class);
                startActivity(intent);
                break;
                */
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
        btn2.setOnClickListener(this);
        //btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    private void CheckState() {

        if(!sw.isChecked()) {
            ListView listview;
            ListAdapter_achievement adapter;

            adapter = new ListAdapter_achievement();

            listview = (ListView) findViewById(R.id.listview1);
            listview.setAdapter(adapter);

            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Potato Master 1", "Write more than 10 reviews for Potato Chips", "15 / 10", 15, 10);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Potato Master 2", "Write more than 10 reviews for Potato Chips", "15 / 20", 15, 20);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Potato Master 3", "Write more than 10 reviews for Potato Chips", "15 / 30", 15, 30);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Potato Master 4", "Write more than 10 reviews for Potato Chips", "15 / 40", 15, 40);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Potato Master 5", "Write more than 10 reviews for Potato Chips", "15 / 50", 15, 50);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Potato Master 6", "Write more than 10 reviews for Potato Chips", "15 / 60", 15, 60);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Potato Master 7", "Write more than 10 reviews for Potato Chips", "15 / 70", 15, 70);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Potato Master 8", "Write more than 10 reviews for Potato Chips", "15 / 80", 15, 80);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Potato Master 9", "Write more than 10 reviews for Potato Chips", "15 / 90", 15, 90);
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Potato Master 10", "Write more than 10 reviews for Potato Chips", "15 / 100", 15, 100);

        }
        else {
            ListView listview;
            ListAdapter_achievement adapter;

            adapter = new ListAdapter_achievement();

            listview = (ListView) findViewById(R.id.listview1);
            listview.setAdapter(adapter);

            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.prize), "Potato Master 1", "Write more than 10 reviews for Potato Chips", "15 / 10", 15, 10);


        }
    }

}

