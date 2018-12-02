package com.youdaike.checkticket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youdaike.checkticket.R;
import com.youdaike.checkticket.model.ReportModel;

import java.util.List;

/**
 * Created by yuanxx on 2016/10/15.
 */
public class ReportAdapter extends BaseAdapter {
    private Context mContext;
    private List<ReportModel> mList;

    public ReportAdapter(Context context, List<ReportModel> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_report, null);
            holder.tv_date = (TextView) convertView.findViewById(R.id.item_report_date);
            holder.tv_title = (TextView) convertView.findViewById(R.id.item_report_title);
            holder.tv_num = (TextView) convertView.findViewById(R.id.item_report_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_date.setText(mList.get(i).getDate());
        holder.tv_title.setText(mList.get(i).getTitle());
        holder.tv_num.setText(mList.get(i).getNum());
        return convertView;
    }

    class ViewHolder {
        TextView tv_date;
        TextView tv_title;
        TextView tv_num;
    }
}