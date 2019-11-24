package com.example.snackking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

// 검색했을 때 나오는 목록의 한 칸씩 다루는 부분 with recommendation_snack_view.xml

public class Recommendation_Adapter extends BaseAdapter {

    private Context context;
    private List<Response_DataStructure> list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;

    public Recommendation_Adapter(List<Response_DataStructure> list, Context context){
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        if(list == null) return 0;
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
            convertView = inflate.inflate(R.layout.recommendation_snack_view,null);

            viewHolder = new ViewHolder();
            viewHolder.label_user = (TextView) convertView.findViewById(R.id.label_respond_user_id);
            viewHolder.label_snack = (TextView) convertView.findViewById(R.id.label_respond_snack);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.label_user.setText(list.get(position).getuser());
        viewHolder.label_snack.setText(list.get(position).getsnack());

        return convertView;
    }

    class ViewHolder{
        public TextView label_user;
        public TextView label_snack;
    }

}