package com.example.snackking;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter_achievement extends BaseAdapter {
    private ArrayList<AchievementData> achievementDataList = new ArrayList();

    public ListAdapter_achievement(/*ArrayList<AchievementData> data*/) {

    }

    @Override
    public int getCount() {
        return achievementDataList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_achievement, parent, false);
        }

        ImageView achImageView = (ImageView) convertView.findViewById(R.id.imageView);
        ProgressBar progressbar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        TextView nameTextView = (TextView) convertView.findViewById(R.id.textName);
        TextView descriptionTextView = (TextView) convertView.findViewById(R.id.textCondition);
        TextView numberTextView = (TextView) convertView.findViewById(R.id.textNumber);

        AchievementData achievementData = achievementDataList.get(position);

        progressbar.setMax(achievementData.goalstate);
        progressbar.setProgress(achievementData.currentstate);
        achImageView.setImageDrawable(achievementData.achImage);
        nameTextView.setText(achievementData.strName);
        descriptionTextView.setText(achievementData.strDescription);
        numberTextView.setText(achievementData.strNumber);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return achievementDataList.get(position);
    }

    public void addItem(Drawable arc, String name, String description, String number, int currentstate, int goalstate) {
        AchievementData item = new AchievementData();

        item.achImage = arc;
        item.strName = name;
        item.strDescription = description;
        item.strNumber = number;
        item.currentstate = currentstate;
        item.goalstate = goalstate;

        achievementDataList.add(item);

    }
}
