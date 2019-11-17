package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

public class Snack_Info extends AppCompatActivity {

    private String snack_name;
    private String snack_taste;
    private String snack_cost;
    private String snack_number_of_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_info);

        TextView info_snack_name = (TextView)findViewById(R.id.info_snack_name);
        TextView info_snack_taste = (TextView)findViewById(R.id.info_snack_taste);
        TextView info_snack_cost = (TextView)findViewById(R.id.info_snack_cost);

        Intent intent = getIntent();

        snack_name = intent.getStringExtra("name");
        snack_taste = intent.getStringExtra("taste");
        snack_cost = intent.getStringExtra("cost");
        snack_number_of_rate = intent.getStringExtra("number");

        info_snack_name.setText(snack_name);
        info_snack_taste.setText(snack_taste);
        info_snack_cost.setText(snack_cost);

        Button button_review = (Button)findViewById(R.id.button_write_review);
        button_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent review_intent = new Intent(getApplicationContext(), Snack_Review.class);
                review_intent.putExtra(("name"), snack_name);
                review_intent.putExtra(("taste"), snack_taste);
                review_intent.putExtra(("cost"), snack_cost);
                review_intent.putExtra(("number"), snack_number_of_rate);
                startActivity(review_intent);
            }
        });

        Button button_info_back = (Button)findViewById(R.id.button_info_back);
        button_info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        /*Button button_review = (Button)findViewById(R.id.button_write_review);
        button_review.setText("Revise Review");*/
    }
}
