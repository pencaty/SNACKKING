package com.example.snackking;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Show_Snacks extends RecyclerView.Adapter<Show_Snacks.CustomViewHolder> { // show snacks stored in DB through PHP file

    private ArrayList<Snack_DataStructure> mList = null;
    private Activity context = null;

    public Show_Snacks(Activity context, ArrayList<Snack_DataStructure> list) {
        this.context = context;
        this.mList = list;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView name;
        protected TextView taste;
        protected TextView cost;
        protected TextView number_of_rate;
        protected TextView keyword1;
        protected TextView keyword2;
        protected TextView keyword3;


        public CustomViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.textView_list_name);
            this.taste = (TextView) view.findViewById(R.id.textView_list_taste);
            this.cost = (TextView) view.findViewById(R.id.textView_list_cost);
            this.number_of_rate = (TextView) view.findViewById(R.id.textView_list_number_of_rate);
            this.keyword1 = (TextView) view.findViewById((R.id.textView_list_keyword1));
            this.keyword2 = (TextView) view.findViewById((R.id.textView_list_keyword2));
            this.keyword3 = (TextView) view.findViewById((R.id.textView_list_keyword3));
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.name.setText(mList.get(position).getSnack_name());
        viewholder.taste.setText("Taste : " + mList.get(position).getSnack_taste() + " / 5 ");
        viewholder.cost.setText("Cost : " + mList.get(position).getSnack_cost() + " / 5 ");
        viewholder.number_of_rate.setText("Vote : " + mList.get(position).getSnack_number_of_rate());
        viewholder.keyword1.setText("Keywords : " + mList.get(position).getSnack_keyword_1());
        viewholder.keyword2.setText(mList.get(position).getSnack_keyword_2());
        viewholder.keyword3.setText(mList.get(position).getSnack_keyword_3());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}

