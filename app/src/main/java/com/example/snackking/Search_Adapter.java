package com.example.snackking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class Search_Adapter extends BaseAdapter {

    private Context context;
    private List<Snack_DataStructure> list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;

    public Search_Adapter(List<Snack_DataStructure> list, Context context){
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = inflate.inflate(R.layout.search_snack_row_view,null);

            viewHolder = new ViewHolder();
            viewHolder.label_name = (TextView) convertView.findViewById(R.id.label_snack_name);
            viewHolder.label_taste = (TextView) convertView.findViewById(R.id.label_snack_taste);
            viewHolder.label_cost = (TextView) convertView.findViewById(R.id.label_snack_cost);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.label_name.setText(list.get(position).getSnack_name());
        viewHolder.label_taste.setText(list.get(position).getSnack_taste());
        viewHolder.label_cost.setText(list.get(position).getSnack_cost());

        return convertView;
    }

    class ViewHolder{
        public TextView label_name;
        public TextView label_taste;
        public TextView label_cost;
    }

}