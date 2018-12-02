package com.youdaike.checkticket.fragment;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;
import com.youdaike.checkticket.R;
import com.youdaike.checkticket.activity.MainActivity;
import com.youdaike.checkticket.adapter.ConsumeRecordAdapter;
import com.youdaike.checkticket.model.BackEvent;
import com.youdaike.checkticket.model.BaseResponseModel;
import com.youdaike.checkticket.model.PrintEvent;
import com.youdaike.checkticket.model.TicketQueryPrintModel;
import com.youdaike.checkticket.utils.ResponseUtil;
import com.youdaike.checkticket.view.CustomDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 凭证查询详情
 * Created by yuanxx on 2016/9/2.
 */
public class QueryDetailFragment extends BaseFragment {
    private static final String TAG = "QueryDetailFragment";
    private LinearLayout mRootView;
    private ConsumeRecordAdapter mAdapter;
    private List<TicketQueryPrintModel> mQueryModelList;
    private String mResponseJson;
    private CustomDialog mCustomDialog;
    private boolean mIsSelectAll;

    @BindView(R.id.fragment_query_detail_selectall)
    TextView mTV_SelectAll;
    @BindView(R.id.fragment_query_detail_print)
    TextView mTV_Print;
    @BindView(R.id.fragment_query_detail_record)
    RecyclerView mRV_Record;
    @BindView(R.id.fragment_query_detail_nodata)
    LinearLayout mLL_NoData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResponseJson = getArguments().getString(QueryFragment.RESPONSEJSON);
        BaseResponseModel<TicketQueryPrintModel> model = ResponseUtil.getListResponse(mResponseJson, TicketQueryPrintModel.class);
        mQueryModelList = (List<TicketQueryPrintModel>) model.getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (LinearLayout) inflater.inflate(R.layout.fragment_query_detail, container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        initListener();
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.fragment_query_detail_selectall, R.id.fragment_query_detail_print})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_query_detail_selectall:
                if (mIsSelectAll) {
                    Log.i(TAG, "onClick: 取消全选");
                    mIsSelectAll = false;
                    mAdapter.selectAll(false);
                } else {
                    Log.i(TAG, "onClick: 全选");
                    mIsSelectAll = true;
                    mAdapter.selectAll(true);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.fragment_query_detail_print:
                List<TicketQueryPrintModel> list = mAdapter.getSelectData();
                Log.i(TAG, "onClick: " + list.size());
                if (list != null && list.size() > 0) {
                    Gson gson = new Gson();
                    String json = gson.toJson(list);
                    mTV_Print.setEnabled(false);
                    mCustomDialog = CustomDialog.show(getActivity(), "正在打印···");
                    ((MainActivity) getActivity()).print(json, 5);
                } else {
                    Toast.makeText(getActivity(), "请选择打印内容", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initView() {
        if (mQueryModelList != null && mQueryModelList.size() > 0) {
            mTV_SelectAll.setEnabled(true);
            mTV_Print.setEnabled(true);
            mRV_Record.setVisibility(View.VISIBLE);
            mLL_NoData.setVisibility(View.GONE);
            mAdapter = new ConsumeRecordAdapter(getActivity(), mQueryModelList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRV_Record.setLayoutManager(layoutManager);
            mRV_Record.setAdapter(mAdapter);
        } else {
            mTV_SelectAll.setEnabled(false);
            mTV_Print.setEnabled(false);
            mRV_Record.setVisibility(View.GONE);
            mLL_NoData.setVisibility(View.VISIBLE);
        }
    }

    private void initListener() {
        ((MainActivity) getActivity()).setOnKeyPressedListener(new MainActivity.OnKeyPressedListener() {
            @Override
            public void onKeyPressed(int keycode) {
                final Resources res = getResources();
                switch (keycode) {
                    case KeyEvent.KEYCODE_BACK:
                        Log.i(TAG, res.getString(R.string.back));
                        getFragmentManager().popBackStack();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PrintEvent event) {
        Log.i(TAG, "onMessageEvent: ");
        if (mCustomDialog != null && mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
        mTV_Print.setEnabled(true);
        switch (event.getFalg()) {
            case 0:
                break;
            case 1:
                ((MainActivity) getActivity()).setFragment(1, null);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BackEvent event) {
        switch (event.getFragment()) {
            case 5:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
