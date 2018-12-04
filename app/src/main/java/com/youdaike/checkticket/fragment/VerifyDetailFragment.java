package com.youdaike.checkticket.fragment;


import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youdaike.checkticket.R;
import com.youdaike.checkticket.activity.MainActivity;
import com.youdaike.checkticket.contract.StringContract;
import com.youdaike.checkticket.contract.UrlContract;
import com.youdaike.checkticket.model.BackEvent;
import com.youdaike.checkticket.model.BaseResponseModel;
import com.youdaike.checkticket.model.PrintEvent;
import com.youdaike.checkticket.model.TicketVerifyModel;
import com.youdaike.checkticket.utils.PreferencesUtil;
import com.youdaike.checkticket.utils.ResponseUtil;
import com.youdaike.checkticket.view.CustomDialog;
import com.youdaike.checkticket.view.NumberKeyboardView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 凭证验证详情
 * Created by yuanxx on 2016/9/2.
 */
public class VerifyDetailFragment extends BaseFragment {
    private static final String TAG = "VerifyDetailFragment";
    private RelativeLayout mRootView;
    private StringBuilder mCountStr;
    private TicketVerifyModel mVerifyModel;
    private int mMinCount = 0;
    private int mTotalCount;
    private String mCouponNo;
    private CustomDialog mCustomDialog;

    @BindView(R.id.fragment_verify_detail_title)
    TextView mTV_Title;
    @BindView(R.id.fragment_verify_detail_number)
    TextView mTV_Number;
    @BindView(R.id.fragment_verify_detail_phone)
    TextView tv_phone;
    @BindView(R.id.fragment_verify_detail_remaincount)
    TextView mTV_RemainCount;
    @BindView(R.id.fragment_verify_detail_mincount_lay)
    LinearLayout mLL_MinCount;
    @BindView(R.id.fragment_verify_detail_mincount)
    TextView mTV_MinCount;
    @BindView(R.id.fragment_verify_detail_count)
    TextView mTV_Count;
    @BindView(R.id.fragment_verify_detail_print)
    CheckBox mCB_Print;
    @BindView(R.id.fragment_verify_detail_submit)
    TextView mTV_Submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCountStr = new StringBuilder("");
        mCouponNo = getArguments().getString(VerifyFragment.COUPONNO);
        mVerifyModel = (TicketVerifyModel) getArguments().getSerializable(VerifyFragment.TICKETVERIFYMODEL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (RelativeLayout) inflater.inflate(R.layout.fragment_verify_detail, container, false);
        ButterKnife.bind(this, mRootView);
        initListener();
        initView();
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

    private void initView() {
        if (mVerifyModel != null) {
            mTV_Title.setText(mVerifyModel.getTitle());
            mTV_Number.setText(mCouponNo);
            if (TextUtils.isEmpty(mVerifyModel.getPhone())){
                tv_phone.setVisibility(View.GONE);
            }else {
                tv_phone.setVisibility(View.VISIBLE);
                tv_phone.setText("手机号："+mVerifyModel.getPhone());
            }
            mTV_RemainCount.setText(mVerifyModel.getSurplusnm());
            if (!TextUtils.isEmpty(mVerifyModel.getMinnum()) && Integer.valueOf(mVerifyModel.getMinnum()) > 0) {
                mLL_MinCount.setVisibility(View.VISIBLE);
                mMinCount = Integer.valueOf(mVerifyModel.getMinnum());
                mTotalCount = Integer.valueOf(mVerifyModel.getSurplusnm());
                mTV_MinCount.setText(mVerifyModel.getMinnum());
                mTV_Count.setText(mVerifyModel.getSurplusnm());
                mCountStr = new StringBuilder(mVerifyModel.getSurplusnm());
            } else {
                mLL_MinCount.setVisibility(View.INVISIBLE);
            }
            mCB_Print.setChecked(((MainActivity) getActivity()).getPrintStatue());
        }
    }

    @OnClick({R.id.fragment_verify_detail_minus, R.id.fragment_verify_detail_add, R.id.fragment_verify_detail_submit, R.id.fragment_verify_detail_print})
    public void onClick(View view) {
        int count;
        if (!TextUtils.isEmpty(mCountStr.toString())) {
            count = Integer.valueOf(mCountStr.toString());
        } else {
            count = 0;
        }
        switch (view.getId()) {
            case R.id.fragment_verify_detail_minus:
                if (count > mMinCount) {
                    count--;
                    mCountStr = new StringBuilder(String.valueOf(count));
                    mTV_Count.setText(mCountStr.toString());
                } else {
                    Toast toast = Toast.makeText(getActivity(), "不能小于最低消费数量", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
                }
                break;
            case R.id.fragment_verify_detail_add:
                if (count < mTotalCount) {
                    count++;
                    mCountStr = new StringBuilder(String.valueOf(count));
                    mTV_Count.setText(mCountStr.toString());
                } else {
                    Toast toast = Toast.makeText(getActivity(), "余票不足，请修改数量", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
                }
                break;
            case R.id.fragment_verify_detail_submit:
                if (TextUtils.isEmpty(mCountStr.toString()) || Integer.valueOf(mCountStr.toString()) < mMinCount) {
                    Toast toast = Toast.makeText(getActivity(), "请输入使用数量", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
                } else {
                    mTV_Submit.setEnabled(false);
                    consume();
                }
                break;
            case R.id.fragment_verify_detail_print:
//                if (!PreferencesUtil.getBoolean(getActivity(), StringContract.ISHDXDEVICE, false)) {
//                    mCB_Print.setChecked(false);
//                    Toast.makeText(getActivity(), "设备不支持打印", Toast.LENGTH_SHORT).show();
//                }
                mCB_Print.setChecked(((MainActivity) getActivity()).getPrintStatue());
                break;
        }
    }

    private void initListener() {
        ((MainActivity) getActivity()).setOnKeyPressedListener(new MainActivity.OnKeyPressedListener() {
            @Override
            public void onKeyPressed(int keycode) {
                final Resources res = getResources();
                switch (keycode) {
                    case KeyEvent.KEYCODE_F1:
                        Log.i(TAG, "F1");
                        break;
                    case KeyEvent.KEYCODE_F2:
                        Log.i(TAG, "F2");
                        break;
                    case KeyEvent.KEYCODE_F3:
                        Log.i(TAG, "F3");
                        break;
                    case KeyEvent.KEYCODE_F12:
                        Log.i(TAG, res.getString(R.string.scan_left));
                        break;
                    case KeyEvent.KEYCODE_F11:
                        Log.i(TAG, res.getString(R.string.scan));
                        break;
                    case KeyEvent.KEYCODE_CALL:
                        Log.i(TAG, res.getString(R.string.call));
                        break;
                    case KeyEvent.KEYCODE_ENDCALL:
                        Log.i(TAG, res.getString(R.string.end_call));
                        break;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        Log.i(TAG, res.getString(R.string.up));
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        Log.i(TAG, res.getString(R.string.down));
                        break;
                    case KeyEvent.KEYCODE_ENTER:
                        Log.i(TAG, res.getString(R.string.enter));
                        if (TextUtils.isEmpty(mCountStr.toString()) || Integer.valueOf(mCountStr.toString()) < mMinCount) {
                            Toast toast = Toast.makeText(getActivity(), "请输入使用数量", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 20);
                            toast.show();
                        } else {
                            consume();
                        }
                        break;
                    case KeyEvent.KEYCODE_HOME:
                        Log.i(TAG, res.getString(R.string.home));
                        break;
                    case KeyEvent.KEYCODE_DEL:
                        Log.i(TAG, res.getString(R.string.del));
                        if (mCountStr.length() > 0) {
                            mCountStr.deleteCharAt(mCountStr.length() - 1);
                        }
                        break;
                    case KeyEvent.KEYCODE_0:
                        formatCount("0");
                        break;
                    case KeyEvent.KEYCODE_1:
                        formatCount("1");
                        break;
                    case KeyEvent.KEYCODE_2:
                        formatCount("2");
                        break;
                    case KeyEvent.KEYCODE_3:
                        formatCount("3");
                        break;
                    case KeyEvent.KEYCODE_4:
                        formatCount("4");
                        break;
                    case KeyEvent.KEYCODE_5:
                        formatCount("5");
                        break;
                    case KeyEvent.KEYCODE_6:
                        formatCount("6");
                        break;
                    case KeyEvent.KEYCODE_7:
                        formatCount("7");
                        break;
                    case KeyEvent.KEYCODE_8:
                        formatCount("8");
                        break;
                    case KeyEvent.KEYCODE_9:
                        formatCount("9");
                        break;
                    case KeyEvent.KEYCODE_POUND:
                        Log.i(TAG, "#");
                        break;
                    case KeyEvent.KEYCODE_STAR:
                        Log.i(TAG, "*");
                        break;
                    case KeyEvent.KEYCODE_PERIOD:
                        Log.i(TAG, ".");
                        break;
                    case KeyEvent.KEYCODE_BACK:
                        Log.i(TAG, res.getString(R.string.back));
                        getFragmentManager().popBackStack();
                        break;
                    case KeyEvent.KEYCODE_ESCAPE:
                        Log.i(TAG, res.getString(R.string.esc));
                        break;
                    case KeyEvent.KEYCODE_POWER:
                        Log.i(TAG, res.getString(R.string.power));
                        break;
                    case KeyEvent.KEYCODE_F10:
                        Log.i(TAG, "fn");
                        break;
                    case KeyEvent.KEYCODE_F9:
                        Log.i(TAG, res.getString(R.string.letter));
                        break;
                    case KeyEvent.KEYCODE_CAMERA:
                        Log.i(TAG, res.getString(R.string.camera));
                        break;
                    case KeyEvent.KEYCODE_VOLUME_DOWN:
                        Log.i(TAG, res.getString(R.string.vol_dec));
                        break;
                    case KeyEvent.KEYCODE_VOLUME_UP:
                        Log.i(TAG, res.getString(R.string.vol_inc));
                        break;
                    default:
                        break;
                }
                Log.i(TAG, "mCountStr=" + mCountStr.toString());
                mTV_Count.setText(mCountStr.toString());
            }
        });
    }

    private void formatCount(String number) {
        //清除初始化的数据
        if (mCountStr.length() > 0 && mVerifyModel != null && mCountStr.toString().equals(mVerifyModel.getSurplusnm())) {
            mCountStr.delete(0, mCountStr.length());
            mCountStr.append(number);
        } else {
            mCountStr.append(number);
        }
    }

    private void consume() {
        Log.i(TAG, "consume: =" + UrlContract.TICKET_CONSUME + "?devicesid=" + getIMEI() + "&couponno=" + mCouponNo + "&Num=" + mCountStr.toString());
        mCustomDialog = CustomDialog.show(getActivity(), "正在打印凭证，请勿重复提交…");
        OkHttpUtils
                .post()
                .url(UrlContract.TICKET_CONSUME)
                .addParams("devicesid", getIMEI())
                .addParams("couponno", mCouponNo)
                .addParams("Num", mCountStr.toString())
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
                        Toast toast = Toast.makeText(getActivity(), "网络异常，请稍后重试", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 20);
                        toast.show();
                        mTV_Submit.setEnabled(true);
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.i(TAG, "onResponse: response=" + response);
                        if (mCustomDialog != null && mCustomDialog.isShowing()) {
                            mCustomDialog.dismiss();
                        }
                        BaseResponseModel<TicketVerifyModel> model = ResponseUtil.getObjectResponse(String.valueOf(response), TicketVerifyModel.class);
                        if ("200".equals(model.getStatus())) {//成功
                            Log.i(TAG, "onResponse: 成功");
                            Toast toast = Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 20);
                            toast.show();
                            mTV_Submit.setEnabled(true);
                            if (mCB_Print.isChecked()) {
                                getPrintFormat();
                            } else {
                                ((MainActivity) getActivity()).setFragment(0, null);
                            }
                        } else {//失败
                            Log.i(TAG, "onResponse: 失败---" + model.getStatus() + "," + model.getMessage());
                            Toast toast = Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 20);
                            toast.show();
                            mTV_Submit.setEnabled(true);
                        }
                    }
                });
    }

    private void getPrintFormat() {
        Log.i(TAG, "getPrintFormat: =" + UrlContract.GET_PRINT_FORMAT + "?devicesid=" + getIMEI() + "&couponno=" + mCouponNo);
        OkHttpUtils
                .post()
                .url(UrlContract.GET_PRINT_FORMAT)
                .addParams("devicesid", getIMEI())
                .addParams("couponno", mCouponNo)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        return response.body().string();
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.i(TAG, "onError: " + e);
                        Toast toast = Toast.makeText(getActivity(), "网络异常，请稍后重试", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 20);
                        toast.show();
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.i(TAG, "onResponse: response=" + response);
                        BaseResponseModel<TicketVerifyModel> model = ResponseUtil.getObjectResponse(String.valueOf(response), TicketVerifyModel.class);
                        if ("200".equals(model.getStatus())) {//成功
                            Log.i(TAG, "onResponse: 成功");
                            ((MainActivity) getActivity()).print(String.valueOf(response), 1);
                        } else {//失败
                            Log.i(TAG, "onResponse: 失败---" + model.getStatus() + "," + model.getMessage());
                            Toast toast = Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.BOTTOM, 0, 20);
                            toast.show();
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
        switch (event.getFalg()) {
            case 0:
                break;
            case 1:
                ((MainActivity) getActivity()).setFragment(0, null);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BackEvent event) {
        switch (event.getFragment()) {
            case 4:
                getFragmentManager().popBackStack();
                break;
        }
    }
}
