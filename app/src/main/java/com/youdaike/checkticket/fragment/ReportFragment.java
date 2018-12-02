package com.youdaike.checkticket.fragment;


import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.youdaike.checkticket.R;
import com.youdaike.checkticket.activity.MainActivity;
import com.youdaike.checkticket.adapter.ReportAdapter;
import com.youdaike.checkticket.contract.StringContract;
import com.youdaike.checkticket.contract.UrlContract;
import com.youdaike.checkticket.model.BackEvent;
import com.youdaike.checkticket.model.BaseResponseModel;
import com.youdaike.checkticket.model.PrintEvent;
import com.youdaike.checkticket.model.ReportModel;
import com.youdaike.checkticket.model.ReportModel;
import com.youdaike.checkticket.model.ReportModel;
import com.youdaike.checkticket.utils.PreferencesUtil;
import com.youdaike.checkticket.utils.ResponseUtil;
import com.youdaike.checkticket.view.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 统计报表
 * Created by yuanxx on 2016/9/2.
 */
public class ReportFragment extends BaseFragment {
    private static final String TAG = "ReportFragment";
    private LinearLayout mRootView;
    private String mSearchType;
    private int mYear, mMonth, mDay, mType;
    private long mBeginTime, mEndTime;
    private List<ReportModel> mList;
    private ReportAdapter mAdapter;
    private String mResponse;
    private CustomDialog mCustomDialog;

    @BindView(R.id.fragment_report_start)
    TextView mTV_Start;
    @BindView(R.id.fragment_report_end)
    TextView mTV_End;
    @BindView(R.id.fragment_report_list)
    ListView mLV_List;
    @BindView(R.id.fragment_report_nodata)
    LinearLayout mLL_NoData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchType = "1";
        mBeginTime = 0;
        mEndTime = 0;
        report();
        mList = new ArrayList<>();
        mAdapter = new ReportAdapter(getActivity(), mList);
        //初始化Calendar日历对象
        Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
        Date mydate = new Date(); //获取当前日期Date对象
        mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期
        mYear = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
        mMonth = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
        mDay = mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (LinearLayout) inflater.inflate(R.layout.fragment_report, container, false);
        ButterKnife.bind(this, mRootView);
        mLV_List.setAdapter(mAdapter);
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

    @OnClick({R.id.fragment_report_query, R.id.fragment_report_start_lay, R.id.fragment_report_end_lay, R.id.fragment_report_print})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_report_query:
                Log.i(TAG, "onClick: 立即查询");
                if (mBeginTime == 0 && mEndTime == 0) {
                    Toast.makeText(getActivity(), "请选择日期", Toast.LENGTH_SHORT).show();
                    return;
                }
                //只选择一个日期时，另一个日期默认等于选择的日期，效果为查询某一天的数据
                if (mBeginTime != 0 && mEndTime == 0) {
                    mEndTime = mBeginTime;
                }
                if (mBeginTime == 0 && mEndTime != 0) {
                    mBeginTime = mEndTime;
                }
                mSearchType = "0";
                report();
                break;
            case R.id.fragment_report_start_lay:
                Log.i(TAG, "onClick: 开始日期");
                mType = 1;
                showDatePicker();
                break;
            case R.id.fragment_report_end_lay:
                Log.i(TAG, "onClick: 结束日期");
                mType = 2;
                showDatePicker();
                break;
            case R.id.fragment_report_print:
                Log.i(TAG, "onClick: 打印=" + mResponse);
                if (!TextUtils.isEmpty(mResponse)) {
                    mCustomDialog = CustomDialog.show(getActivity(), "打印中···");
                    ((MainActivity) getActivity()).print(String.valueOf(mResponse), 4);
                } else {
                    Toast.makeText(getActivity(), "暂无记录", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void initListener() {
        ((MainActivity) getActivity()).setOnKeyPressedListener(new MainActivity.OnKeyPressedListener() {
            @Override
            public void onKeyPressed(int keycode) {
                final Resources res = getResources();
                switch (keycode) {
                    case KeyEvent.KEYCODE_BACK:
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), onDateSetListener, mYear, mMonth, mDay);
        dialog.show();//显示DatePickerDialog组件
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //修改year、month、day的变量值，以便以后单击按钮时，DatePickerDialog上显示上一次修改后的值
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            //更新日期
            updateDate();
        }

        //当DatePickerDialog关闭时，更新日期显示
        private void updateDate() {
            Log.i(TAG, "updateDate: " + mYear + "-" + (mMonth + 1) + "-" + mDay);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = simpleDateFormat.parse(mYear + "-" + (mMonth + 1) + "-" + mDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //在TextView上显示日期
            if (mType == 1) {
                mTV_Start.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
                mTV_Start.setTextColor(getResources().getColor(R.color.color_555555));
                mBeginTime = date.getTime() / 1000;
            } else if (mType == 2) {
                mTV_End.setText(mYear + "-" + (mMonth + 1) + "-" + mDay);
                mTV_End.setTextColor(getResources().getColor(R.color.color_555555));
                mEndTime = date.getTime() / 1000;
            }
        }
    };

    private void report() {
        Log.i(TAG, "report: =" + UrlContract.REPORT_QUERY + "?devicesid=" + getIMEI() + "&seachtype=" + mSearchType + "&begintime=" + (mBeginTime == 0 ? "" : String.valueOf(mBeginTime)) + "&endtime=" + (mEndTime == 0 ? "" : String.valueOf(mEndTime)));
        mCustomDialog = CustomDialog.show(getActivity(), "正在查询，请稍候···");
        OkHttpUtils
                .post()
                .url(UrlContract.REPORT_QUERY)
                .addParams("devicesid", getIMEI())
                .addParams("seachtype", mSearchType)
                .addParams("begintime", mBeginTime == 0 ? "" : String.valueOf(mBeginTime))
                .addParams("endtime", mEndTime == 0 ? "" : String.valueOf(mEndTime))
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        return response.body().string();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i(TAG, "onError: " + e);
                        if (mCustomDialog != null && mCustomDialog.isShowing()) {
                            mCustomDialog.dismiss();
                        }
                        mLV_List.setVisibility(View.GONE);
                        mLL_NoData.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.i(TAG, "onResponse: response=" + response);
                        if (mCustomDialog != null && mCustomDialog.isShowing()) {
                            mCustomDialog.dismiss();
                        }
                        BaseResponseModel<ReportModel> model = ResponseUtil.getListResponse(String.valueOf(response), ReportModel.class);
                        if ("200".equals(model.getStatus())) {//成功
                            Log.i(TAG, "onResponse: 成功");
                            mList = (List<ReportModel>) model.getData();
                            if (mList != null && mList.size() > 0) {
                                mLV_List.setVisibility(View.VISIBLE);
                                mLL_NoData.setVisibility(View.GONE);
                                mAdapter.setList(mList);
                                mResponse = String.valueOf(response);
                            } else {
                                mLV_List.setVisibility(View.GONE);
                                mLL_NoData.setVisibility(View.VISIBLE);
                            }
                        } else {//失败
                            Log.i(TAG, "onResponse: 失败---" + model.getStatus() + "," + model.getMessage());
                            mLV_List.setVisibility(View.GONE);
                            mLL_NoData.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT).show();
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BackEvent event) {
        switch (event.getFragment()){
            case 3:
                if (getActivity() != null) {
                    getActivity().finish();
                }
                break;
        }
    }
}
