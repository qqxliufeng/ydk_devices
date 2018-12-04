package com.youdaike.checkticket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.youdaike.checkticket.R;
import com.youdaike.checkticket.model.TicketQueryPrintModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yuanxx on 2016/9/25.
 */
public class ConsumeRecordAdapter extends RecyclerView.Adapter<ConsumeRecordAdapter.MyViewHolder> {
    private static final String TAG = "ConsumeRecordAdapter";
    private Context mContext;
    private List<TicketQueryPrintModel> mList;
    private HashMap<Integer, Boolean> isSelected;

    public ConsumeRecordAdapter(Context context, List<TicketQueryPrintModel> list) {
        mContext = context;
        mList = list;
        isSelected = new HashMap<Integer, Boolean>();
        initMap();
    }

    // 初始化isSelected的数据
    private void initMap() {
        for (int i = 0; i < mList.size(); i++) {
            isSelected.put(i, false);
        }
    }

    public void selectAll(boolean isSelectAll) {
        if (isSelectAll) {
            for (int i = 0; i < mList.size(); i++) {
                isSelected.put(i, true);
            }
        } else {
            for (int i = 0; i < mList.size(); i++) {
                isSelected.put(i, false);
            }
        }
    }

    public List<TicketQueryPrintModel> getSelectData() {
        List<TicketQueryPrintModel> list = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if (isSelected.get(i)) {
                list.add(mList.get(i));
            }
        }
        return list;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(ConsumeRecordAdapter.MyViewHolder holder, int position) {
        final int _position = position;
        holder.cb_check.setChecked(isSelected.get(position));
        holder.tv_ticket_name.setText(mList.get(position).getTeamtitle());
        holder.tv_consume_count.setText(mList.get(position).getCouponnum());
        holder.tv_status.setText("已消费");
        holder.tv_customer.setText(mList.get(position).getOrderlink());
        holder.tv_num.setText(mList.get(position).getCouponno());
        holder.tv_phone.setText(mList.get(position).getOrdermoblie());
        holder.tv_buy_time.setText(mList.get(position).getOrdertime());
        holder.tv_consume_time.setText(mList.get(position).getCreatetime());
        holder.tv_period.setText(mList.get(position).getTeamtotime());
        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: " + _position);
                if (isSelected.get(_position)) {
                    isSelected.put(_position, false);
                } else {
                    isSelected.put(_position, true);
                }
                notifyItemChanged(_position);
            }
        });
    }

    @Override
    public ConsumeRecordAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_consume_record, parent, false));
        return holder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_layout;
        CheckBox cb_check;
        TextView tv_ticket_name;
        TextView tv_consume_count;
        TextView tv_status;
        TextView tv_customer;
        TextView tv_num;
        TextView tv_phone;
        TextView tv_buy_time;
        TextView tv_consume_time;
        TextView tv_period;

        public MyViewHolder(View view) {
            super(view);
            rl_layout = (RelativeLayout) view.findViewById(R.id.item_consume_record_layout);
            cb_check = (CheckBox) view.findViewById(R.id.item_consume_record_check);
            tv_ticket_name = (TextView) view.findViewById(R.id.item_consume_record_ticket_name);
            tv_consume_count = (TextView) view.findViewById(R.id.item_consume_record_consume_count);
            tv_status = (TextView) view.findViewById(R.id.item_consume_record_status);
            tv_customer = (TextView) view.findViewById(R.id.item_consume_record_customer);
            tv_num = (TextView) view.findViewById(R.id.item_consume_record_num);
            tv_phone = (TextView) view.findViewById(R.id.item_consume_record_phone);
            tv_buy_time = (TextView) view.findViewById(R.id.item_consume_record_buy_time);
            tv_consume_time = (TextView) view.findViewById(R.id.item_consume_record_consume_time);
            tv_period = (TextView) view.findViewById(R.id.item_consume_record_period);
        }

    }
}
