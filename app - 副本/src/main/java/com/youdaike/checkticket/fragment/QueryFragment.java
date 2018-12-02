package com.youdaike.checkticket.fragment;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;
import com.youdaike.checkticket.R;
import com.youdaike.checkticket.activity.MainActivity;
import com.youdaike.checkticket.contract.UrlContract;
import com.youdaike.checkticket.model.BackEvent;
import com.youdaike.checkticket.model.BaseResponseModel;
import com.youdaike.checkticket.model.TicketQueryPrintModel;
import com.youdaike.checkticket.utils.Constants;
import com.youdaike.checkticket.utils.ResponseUtil;
import com.youdaike.checkticket.utils.liandi.impl.camera.CameraScannerImpl;
import com.youdaike.checkticket.view.CustomDialog;
import com.youdaike.checkticket.view.NumberKeyboardView;
import com.youdaike.zxinglib.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * 凭证查询
 * Created by yuanxx on 2016/9/2.
 */
public class QueryFragment extends BaseFragment {
    private static final String TAG = "QueryFragment";
    public static final String RESPONSEJSON = "ResponseJson";
    private RelativeLayout mRootView;
    private StringBuilder mNumber;
    private CustomDialog mCustomDialog;


    //联迪设备
    private CameraScannerImpl scanner;

    @BindView(R.id.fragment_query_number)
    EditText mET_Number;
    @BindView(R.id.fragment_query_scan)
    ImageView mIV_Scan;
    @BindView(R.id.fragment_query_keyboard)
    NumberKeyboardView mNKV_Keyboard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNumber = new StringBuilder("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (RelativeLayout) inflater.inflate(R.layout.fragment_query, container, false);
        ButterKnife.bind(this, mRootView);
//        mET_Number.setInputType(InputType.TYPE_NULL);
        //隐藏软键盘并显示光标
        hideSoftInputMethod(mET_Number);
        initListener();
        initKeyboardListener();
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

    @OnClick({R.id.fragment_query_scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_query_scan:
                if (scanner == null) {
                    scanner = new CameraScannerImpl(getActivity()) {
                        @Override
                        protected void onDeviceServiceCrash() {
                            Toast.makeText(getActivity(), "扫描异常", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        protected void displayInfo(String info) {
                            if (!TextUtils.isEmpty(info) && TextUtils.isDigitsOnly(info)) {
                                mET_Number.setText(info);
                                mNumber = new StringBuilder(info);
                                mET_Number.setSelection(info.length());
                            }
                        }

                        @Override
                        protected void toast(String msg) {
                        }
                    };
                }
                scanner.startScan(getActivity(), Constants.Scanner.CAMERA_BACK);
//                Intent intent = new Intent(getActivity(), CaptureActivity.class);
//                startActivityForResult(intent, 0);
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
                        if (mNumber.toString().length() > 0) {
                            query();
                        } else {
                            Toast.makeText(getActivity(), "请输入电子凭证/手机号", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case KeyEvent.KEYCODE_HOME:
                        Log.i(TAG, res.getString(R.string.home));
                        break;
                    case KeyEvent.KEYCODE_DEL:
                        Log.i(TAG, res.getString(R.string.del));
                        if (mNumber.length() > 0) {
                            mNumber.deleteCharAt(mNumber.length() - 1);
                        }
                        break;
                    case KeyEvent.KEYCODE_0:
                        Log.i(TAG, "0");
                        mNumber.append("0");
                        break;
                    case KeyEvent.KEYCODE_1:
                        Log.i(TAG, "1");
                        mNumber.append("1");
                        break;
                    case KeyEvent.KEYCODE_2:
                        mNumber.append("2");
                        Log.i(TAG, "2");
                        break;
                    case KeyEvent.KEYCODE_3:
                        mNumber.append("3");
                        Log.i(TAG, "3");
                        break;
                    case KeyEvent.KEYCODE_4:
                        mNumber.append("4");
                        Log.i(TAG, "4");
                        break;
                    case KeyEvent.KEYCODE_5:
                        Log.i(TAG, "5");
                        mNumber.append("5");
                        break;
                    case KeyEvent.KEYCODE_6:
                        Log.i(TAG, "6");
                        mNumber.append("6");
                        break;
                    case KeyEvent.KEYCODE_7:
                        Log.i(TAG, "7");
                        mNumber.append("7");
                        break;
                    case KeyEvent.KEYCODE_8:
                        Log.i(TAG, "8");
                        mNumber.append("8");
                        break;
                    case KeyEvent.KEYCODE_9:
                        Log.i(TAG, "9");
                        mNumber.append("9");
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
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
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
                mET_Number.setText(mNumber.toString());
                mET_Number.setSelection(mNumber.toString().length());
            }
        });
    }

    private void initKeyboardListener() {
        mNKV_Keyboard.setOnKeyboardPressedListener(new NumberKeyboardView.OnKeyboardPressedListener() {
            @Override
            public void onKeyboardPressed(int key) {
                switch (key) {
                    case R.id.view_keyboard_1:
                        mNumber.append("1");
                        break;
                    case R.id.view_keyboard_2:
                        mNumber.append("2");
                        break;
                    case R.id.view_keyboard_3:
                        mNumber.append("3");
                        break;
                    case R.id.view_keyboard_4:
                        mNumber.append("4");
                        break;
                    case R.id.view_keyboard_5:
                        mNumber.append("5");
                        break;
                    case R.id.view_keyboard_6:
                        mNumber.append("6");
                        break;
                    case R.id.view_keyboard_7:
                        mNumber.append("7");
                        break;
                    case R.id.view_keyboard_8:
                        mNumber.append("8");
                        break;
                    case R.id.view_keyboard_9:
                        mNumber.append("9");
                        break;
                    case R.id.view_keyboard_star:
                        Log.i(TAG, "*");
                        break;
                    case R.id.view_keyboard_0:
                        mNumber.append("0");
                        break;
                    case R.id.view_keyboard_clear:
                        Log.i(TAG, "清空");
                        if (mNumber.length() > 0) {
                            mNumber.delete(0, mNumber.length());
                        }
                        break;
                    case R.id.view_keyboard_right_back:
                        if (getActivity() != null) {
                            getActivity().finish();
                        }
                        break;
                    case R.id.view_keyboard_right_delete:
                        if (mNumber.length() > 0) {
                            mNumber.deleteCharAt(mNumber.length() - 1);
                        }
                        break;
                    case R.id.view_keyboard_right_submit:
                        Log.i(TAG, "查询");
                        if (mNumber.toString().length() > 0) {
                            query();
                        } else {
                            Toast.makeText(getActivity(), "请输入电子凭证/手机号", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                mET_Number.setText(mNumber.toString());
                mET_Number.setSelection(mNumber.toString().length());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            mNumber = new StringBuilder(scanResult);
            mET_Number.setText(scanResult);
            mET_Number.setSelection(scanResult.length());
        }
    }

    // 隐藏系统键盘
    public void hideSoftInputMethod(EditText ed) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }

        if (methodName == null) {
            ed.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(ed, false);
            } catch (NoSuchMethodException e) {
                ed.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void query() {
        Log.i(TAG, "query: =" + UrlContract.TICKET_QUERY_PRINT + "?devicesid=" + getIMEI() + "&couponno=" + mNumber.toString());
        mCustomDialog = CustomDialog.show(getActivity(), "正在查询，请稍候···");
        OkHttpUtils
                .post()
                .url(UrlContract.TICKET_QUERY_PRINT)
                .addParams("devicesid", getIMEI())
                .addParams("couponno", mNumber.toString())
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
                        Toast.makeText(getActivity(), "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.i(TAG, "onResponse: response=" + response);
                        if (mCustomDialog != null && mCustomDialog.isShowing()) {
                            mCustomDialog.dismiss();
                        }
                        BaseResponseModel<TicketQueryPrintModel> model = ResponseUtil.getListResponse(String.valueOf(response), TicketQueryPrintModel.class);
                        if ("200".equals(model.getStatus())) {//成功
                            Log.i(TAG, "onResponse: 成功");
                            ArrayList list = new ArrayList();
                            list.addAll((List<TicketQueryPrintModel>) model.getData());
                            Bundle bundle = new Bundle();
                            bundle.putString(QueryFragment.RESPONSEJSON, String.valueOf(response));
                            ((MainActivity) getActivity()).setFragment(5, bundle);
                        } else {//失败
                            Log.i(TAG, "onResponse: 失败---" + model.getStatus() + "," + model.getMessage());
                            Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BackEvent event) {
        switch (event.getFragment()){
            case 1:
                if (getActivity() != null) {
                    getActivity().finish();
                }
                break;
        }
    }
}
