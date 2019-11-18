package com.example.snackking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Search_Snack_through_keywords extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private int count;
    private CheckBox cb1;
    private CheckBox cb2;
    private CheckBox cb3;
    private CheckBox cb4;

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    private ArrayList<String> keyword_list;
    private ArrayList<TextView> tv_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__snack_through_keywords);

        keyword_list = new ArrayList<>();
        tv_list = new ArrayList<>();

        cb1 = (CheckBox)findViewById(R.id.checkbox_key1);
        cb2 = (CheckBox)findViewById(R.id.checkbox_key2);
        cb3 = (CheckBox)findViewById(R.id.checkbox_key3);
        cb4 = (CheckBox)findViewById(R.id.checkbox_key4);

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


        Button button_search = (Button)findViewById(R.id.button_search);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), Search_Snack_through_keywords.class);
                //startActivity(intent);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

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

        System.out.println("countcountcount  " + count);

        if(count == 3){
            if(!cb1.isChecked()) cb1.setEnabled(false);
            if(!cb2.isChecked()) cb2.setEnabled(false);
            if(!cb3.isChecked()) cb3.setEnabled(false);
            if(!cb4.isChecked()) cb4.setEnabled(false);
        }
        else{
            cb1.setEnabled(true);
            cb2.setEnabled(true);
            cb3.setEnabled(true);
            cb4.setEnabled(true);
        }

    }
}
