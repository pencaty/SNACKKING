package com.example.snackking;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends AppCompatActivity implements View.OnClickListener {

    ImageView btn1;
    ImageView btn2;
    ImageView btn3;
    //ImageButton btn4;

    LinearLayout lay1;
    LinearLayout lay2;
    LinearLayout lay3;
    LinearLayout lay4;

    TextView tb_name;

    private String user_id;

    private Toast toast;
    private int offSetX = 0;
    private int offSetY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");

        this.InitializeView();
        this.SetListener();

        tb_name.setText(user_id);
    }

    public void InitializeView() {
        btn1 = findViewById(R.id.imageButton1);
        btn2 = findViewById(R.id.imageButton2);
        btn3 = findViewById(R.id.imageButton3);
        //btn4 = findViewById(R.id.imageButton4);

        lay1 = findViewById(R.id.edit);
        lay2 = findViewById(R.id.select);
        lay3 = findViewById(R.id.app);
        lay4 = findViewById(R.id.customer);

        tb_name = findViewById(R.id.text_user_name);
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
            case R.id.imageButton2:
                intent = new Intent(this, Recommendation.class);
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
            case R.id.edit:
            case R.id.select:
            case R.id.app:
            case R.id.customer:
                toast = Toast.makeText(getApplicationContext(), "Implementation in progress", Toast.LENGTH_SHORT);
                offSetX = 0;
                offSetY = 0;
                toast.setGravity(Gravity.CENTER, offSetX, offSetY);
                toast.show();
                break;
                /*
            case R.id.imageButton4:
                intent = new Intent(this, Setting.class);
                startActivity(intent);
                break;*/
        }
    }

    public void SetListener() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        //btn4.setOnClickListener(this);

        lay1.setOnClickListener(this);
        lay2.setOnClickListener(this);
        lay3.setOnClickListener(this);
        lay4.setOnClickListener(this);
    }
}
