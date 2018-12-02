package com.youdaike.checkticket.fragment;


import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.youdaike.checkticket.adapter.HistoryAdapter;
import com.youdaike.checkticket.contract.UrlContract;
import com.youdaike.checkticket.model.BackEvent;
import com.youdaike.checkticket.model.BaseResponseModel;
import com.youdaike.checkticket.model.HistoryModel;
import com.youdaike.checkticket.utils.ResponseUtil;
import com.youdaike.checkticket.view.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

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
 * 交易历史
 * Created by yuanxx on 2016/9/2.
 */
public class HistoryFragment extends BaseFragment {
    private static final String TAG = "HistoryFragment";
    private LinearLayout mRootView;
    private String mSearchType;
    private int mYear, mMonth, mDay, mType;
    private long mBeginTime, mEndTime;
    private List<HistoryModel> mList;
    private HistoryAdapter mAdapter;
    private CustomDialog mCustomDialog;

    @BindView(R.id.fragment_history_radiogroup)
    RadioGroup mRG_RadioGroup;
    @BindView(R.id.fragment_history_start)
    TextView mTV_Start;
    @BindView(R.id.fragment_history_end)
    TextView mTV_End;
    @BindView(R.id.fragment_history_list)
    ListView mLV_List;
    @BindView(R.id.fragment_history_nodata)
    LinearLayout mLL_NoData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();
        mAdapter = new HistoryAdapter(getActivity(), mList);
        mSearchType = "1";
        mBeginTime = 0;
        mEndTime = 0;
        history();
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
        mRootView = (LinearLayout) inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, mRootView);
        mLV_List.setAdapter(mAdapter);
        initListener();
        mRG_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == radioGroup.getChildAt(0).getId()) {
                    Log.i(TAG, "今日");
                    mSearchType = "1";
                } else if (i == radioGroup.getChildAt(1).getId()) {
                    Log.i(TAG, "本周");
                    mSearchType = "2";
                } else if (i == radioGroup.getChildAt(2).getId()) {
                    Log.i(TAG, "本月");
                    mSearchType = "3";
                } else if (i == radioGroup.getChildAt(3).getId()) {
                    Log.i(TAG, "上月");
                    mSearchType = "4";
                }
                mBeginTime = 0;
                mEndTime = 0;
                mTV_Start.setText("请选择开始日期");
                mTV_Start.setTextColor(getResources().getColor(R.color.color_c3c3c3));
                mTV_End.setText("请选择结束日期");
                mTV_End.setTextColor(getResources().getColor(R.color.color_c3c3c3));
                history();
            }
        });
        return mRootView;
    }

    @OnClick({R.id.fragment_history_query, R.id.fragment_history_start_lay, R.id.fragment_history_end_lay, R.id.fragment_history_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_history_query:
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
                    mEndTime = mBeginTime;
                }
                mSearchType = "0";
                history();
                break;
            case R.id.fragment_history_start_lay:
                Log.i(TAG, "onClick: 开始日期");
                mType = 1;
                showDatePicker();
                break;
            case R.id.fragment_history_end_lay:
                Log.i(TAG, "onClick: 结束日期");
                mType = 2;
                showDatePicker();
                break;
            case R.id.fragment_history_back:
                Log.i(TAG, "onClick: 返回");
                if (getActivity() != null) {
                    getActivity().finish();
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
                    case KeyEvent.KEYCODE_1:
                        mRG_RadioGroup.check(mRG_RadioGroup.getChildAt(0).getId());
                        break;
                    case KeyEvent.KEYCODE_2:
                        mRG_RadioGroup.check(mRG_RadioGroup.getChildAt(1).getId());
                        break;
                    case KeyEvent.KEYCODE_3:
                        mRG_RadioGroup.check(mRG_RadioGroup.getChildAt(2).getId());
                        break;
                    case KeyEvent.KEYCODE_4:
                        mRG_RadioGroup.check(mRG_RadioGroup.getChildAt(3).getId());
                        break;
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

    private void history() {
        Log.i(TAG, "history: =" + UrlContract.HISTORY_QUERY + "?devicesid=" + getIMEI() + "&seachtype=" + mSearchType + "&begintime=" + (mBeginTime == 0 ? "" : String.valueOf(mBeginTime)) + "&endtime=" + (mEndTime == 0 ? "" : String.valueOf(mEndTime)));
        mCustomDialog = CustomDialog.show(getActivity(), "正在查询请稍候···");
        OkHttpUtils
                .post()
                .url(UrlContract.HISTORY_QUERY)
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
                        BaseResponseModel<HistoryModel> model = ResponseUtil.getListResponse(String.valueOf(response), HistoryModel.class);
                        if ("200".equals(model.getStatus())) {//成功
                            Log.i(TAG, "onResponse: 成功");
                            mList = (List<HistoryModel>) model.getData();
                            if (mList != null && mList.size() > 0) {
                                mLV_List.setVisibility(View.VISIBLE);
                                mLL_NoData.setVisibility(View.GONE);
                                mAdapter.setList(mList);
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
    public void onMessageEvent(BackEvent event) {
        switch (event.getFragment()){
            case 2:
                if (getActivity() != null) {
                    getActivity().finish();
                }
                break;
        }
    }
}
