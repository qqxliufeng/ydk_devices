package com.youdaike.checkticket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.youdaike.checkticket.R;
import com.youdaike.checkticket.model.ReportModel;

import java.util.List;

/**
 * Created by yuanxx on 2016/10/15.
 */
public class QueryAdapter extends BaseAdapter {
    private Context mContext;
    private List<ReportModel> mList;

    public QueryAdapter(Context context, List<ReportModel> list) {
        mContext = context;
        mList = list;
    }

    public void setList(List<ReportModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ReportModel getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_query, null);
            holder.cb_checkbix = (CheckBox) convertView.findViewById(R.id.item_query_checkbox);
            holder.tv_name = (TextView) convertView.findViewById(R.id.item_query_name);
            holder.tv_consume = (TextView) convertView.findViewById(R.id.item_query_consume);
            holder.tv_status = (TextView) convertView.findViewById(R.id.item_query_status);
            holder.tv_person = (TextView) convertView.findViewById(R.id.item_query_person);
            holder.tv_phone = (TextView) convertView.findViewById(R.id.item_query_phone);
            holder.tv_buytime = (TextView) convertView.findViewById(R.id.item_query_buytime);
            holder.tv_consumetime = (TextView) convertView.findViewById(R.id.item_query_consumetime);
            holder.tv_allowdate = (TextView) convertView.findViewById(R.id.item_query_allowdate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(mList.get(i).getDate());
        holder.tv_consume.setText(mList.get(i).getTitle());
        holder.tv_status.setText(mList.get(i).getNum());
        holder.tv_person.setText(mList.get(i).getNum());
        holder.tv_phone.setText(mList.get(i).getNum());
        holder.tv_buytime.setText(mList.get(i).getNum());
        holder.tv_consumetime.setText(mList.get(i).getNum());
        holder.tv_allowdate.setText(mList.get(i).getNum());

        return convertView;
    }

    class ViewHolder {
        CheckBox cb_checkbix;
        TextView tv_name;
        TextView tv_consume;
        TextView tv_status;
        TextView tv_person;
        TextView tv_phone;
        TextView tv_buytime;
        TextView tv_consumetime;
        TextView tv_allowdate;
    }
}
