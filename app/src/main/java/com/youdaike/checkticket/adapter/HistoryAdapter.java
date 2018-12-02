package com.youdaike.checkticket.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youdaike.checkticket.R;
import com.youdaike.checkticket.model.HistoryModel;

import java.util.List;

/**
 * Created by yuanxx on 2016/10/15.
 */
public class HistoryAdapter extends BaseAdapter {
    private static final String TAG = "HistoryAdapter";
    private Context mContext;
    private List<HistoryModel> mList;

    public HistoryAdapter(Context context, List<HistoryModel> list) {
        mContext = context;
        mList = list;
    }

    public void setList(List<HistoryModel> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public HistoryModel getItem(int i) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_history, null);
            holder.tv_date = (TextView) convertView.findViewById(R.id.item_history_date);
            holder.tv_number = (TextView) convertView.findViewById(R.id.item_history_number);
            holder.tv_type = (TextView) convertView.findViewById(R.id.item_history_type);
            holder.tv_count = (TextView) convertView.findViewById(R.id.item_history_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_date.setText(mList.get(i).getCreatetime());
        holder.tv_number.setText(mList.get(i).getCouponid());
        holder.tv_type.setText(mList.get(i).getTitle());
        holder.tv_count.setText(mList.get(i).getCouponnum());

        return convertView;
    }

    class ViewHolder {
        TextView tv_date;
        TextView tv_number;
        TextView tv_type;
        TextView tv_count;
    }
}
