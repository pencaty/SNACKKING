package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;

public class Snack_Info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack__info);

        TextView info_snack_name = (TextView)findViewById(R.id.info_snack_name);
        TextView info_snack_taste = (TextView)findViewById(R.id.info_snack_taste);
        TextView info_snack_cost = (TextView)findViewById(R.id.info_snack_cost);

        Intent intent = getIntent();
        info_snack_name.setText(intent.getStringExtra("name"));
        info_snack_taste.setText(intent.getStringExtra("taste"));
        info_snack_cost.setText(intent.getStringExtra("cost"));


    }
}
