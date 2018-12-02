package com.youdaike.checkticket.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.device.Printer;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;
import com.youdaike.checkticket.R;
import com.youdaike.checkticket.activity.MainActivity;
import com.youdaike.checkticket.contract.StringContract;
import com.youdaike.checkticket.contract.UrlContract;
import com.youdaike.checkticket.model.BackEvent;
import com.youdaike.checkticket.model.BaseResponseModel;
import com.youdaike.checkticket.model.PrintEvent;
import com.youdaike.checkticket.model.PrintEventCunt;
import com.youdaike.checkticket.model.TicketVerifyModel;
import com.youdaike.checkticket.utils.Constants;
import com.youdaike.checkticket.utils.PreferencesUtil;
import com.youdaike.checkticket.utils.ResponseUtil;
import com.youdaike.checkticket.utils.liandi.impl.camera.CameraScannerImpl;
import com.youdaike.checkticket.utils.liandi.impl.print.PrinterImpl;
import com.youdaike.checkticket.view.CustomDialog;
import com.youdaike.checkticket.view.NumberKeyboardView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hdx.HdxUtil;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * 凭证验证
 * Created by yuanxx on 2016/9/2.
 */
public class VerifyFragment extends BaseFragment {
    private static final String TAG = "VerifyFragment";
    public static final String TICKETVERIFYMODEL = "TicketVerifyModel";
    public static final String COUPONNO = "CouponNo";
    private RelativeLayout mRootView;
    private StringBuilder mNumber;
    private CustomDialog mCustomDialog;

    //联迪设备
    private CameraScannerImpl scanner;
    private PrinterImpl printer;

    @BindView(R.id.fragment_verify_print)
    TextView mTV_Print;
    @BindView(R.id.fragment_verify_number)
    EditText mET_Number;
    @BindView(R.id.fragment_verify_scan)
    ImageView mIV_Scan;
    @BindView(R.id.fragment_verify_keyboard)
    NumberKeyboardView mNKV_Keyboard;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNumber = new StringBuilder("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = (RelativeLayout) inflater.inflate(R.layout.fragment_verify, container, false);
        ButterKnife.bind(this, mRootView);
//        mET_Number.setInputType(InputType.TYPE_NULL);
        //隐藏软键盘并显示光标
        hideSoftInputMethod(mET_Number);
        if (PreferencesUtil.getBoolean(getActivity(), StringContract.ISHDXDEVICE, false)) {
            HdxUtil.SetCameraBacklightness(0);
        }
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

    @OnClick({R.id.fragment_verify_print, R.id.fragment_verify_scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_verify_print:
                Log.i(TAG, "onClick: 打印最后一次消费");
                mCustomDialog = CustomDialog.show(getActivity(), "打印中···");
                mTV_Print.setEnabled(false);
                printLastConsume();
                break;
            case R.id.fragment_verify_scan:
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
                                mET_Number.setSelection(info.length());
                                if (!TextUtils.isEmpty(mET_Number.getText().toString())) {
                                    mNumber = new StringBuilder(mET_Number.getText().toString());
                                    mCustomDialog = CustomDialog.show(getActivity(), "正在跳转，请勿重复提交…");
                                    verify();
                                } else {
                                    Toast.makeText(getActivity(), "请输入电子凭证/手机号", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        protected void toast(String msg) {
                        }
                    };
                }
                int cameraId = PreferencesUtil.getInt(getActivity(), Constants.PreferencesTag.SCANNER_CAMERA, -1);
                if (cameraId == -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setItems(new String[]{"前置摄像头", "后置摄像头"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0){
                                PreferencesUtil.getInt(getActivity(), Constants.PreferencesTag.SCANNER_CAMERA, Constants.Scanner.CAMERA_FRONT);
                                scanner.startScan(getActivity(),Constants.Scanner.CAMERA_FRONT);
                            }else {
                                PreferencesUtil.getInt(getActivity(), Constants.PreferencesTag.SCANNER_CAMERA, Constants.Scanner.CAMERA_BACK);
                                scanner.startScan(getActivity(),Constants.Scanner.CAMERA_BACK);
                            }
                        }
                    });
                    builder.create().show();
                }else {
                    scanner.startScan(getActivity(),cameraId);
                }
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
                            mCustomDialog = CustomDialog.show(getActivity(), "正在跳转，请勿重复提交…");
                            verify();
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
                        Log.i(TAG, "验证");
                        if (!TextUtils.isEmpty(mET_Number.getText().toString())) {
                            mNumber = new StringBuilder(mET_Number.getText().toString());
                            mCustomDialog = CustomDialog.show(getActivity(), "正在跳转，请勿重复提交…");
                            verify();
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

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i(TAG, "onHiddenChanged: " + hidden);
        if (hidden) {
            this.onPause();
        } else {
            this.onResume();
        }
    }


    private void verify() {
        Log.i(TAG, "verify: =" + UrlContract.TICKET_VERIFY + "?devicesid=" + getIMEI() + "&couponno=" + mNumber.toString());
        OkHttpUtils
                .post()
                .url(UrlContract.TICKET_VERIFY)
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
                        BaseResponseModel<TicketVerifyModel> model = ResponseUtil.getObjectResponse(String.valueOf(response), TicketVerifyModel.class);
                        if ("200".equals(model.getStatus())) {//成功
                            Log.i(TAG, "onResponse: 成功");
                            Bundle bundle = new Bundle();
                            bundle.putString(VerifyFragment.COUPONNO, mNumber.toString());
                            bundle.putSerializable(VerifyFragment.TICKETVERIFYMODEL, model.getData());
                            ((MainActivity) getActivity()).setFragment(4, bundle);
                        } else {//失败
                            Log.i(TAG, "onResponse: 失败---" + model.getStatus() + "," + model.getMessage());
//                            Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT).show();
                            //Toast 字体调大
                            Toast toast = Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT);
                            LinearLayout linearLayout = (LinearLayout) toast.getView();
                            TextView messageTextView = (TextView) linearLayout.getChildAt(0);
                            messageTextView.setTextSize(25);
                            toast.show();
                        }
                    }
                });
    }

    private void printLastConsume() {
        Log.i(TAG, "printLastConsume: =" + UrlContract.PRINT_LAST_CONSUME + "?devicesid=" + getIMEI());
        OkHttpUtils
                .post()
                .url(UrlContract.PRINT_LAST_CONSUME)
                .addParams("devicesid", getIMEI())
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
                        mTV_Print.setEnabled(true);
                        mTV_Print.setText("打印最后一次消费");
                        Toast.makeText(getActivity(), "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.i(TAG, "onResponse: response=" + response);
                        BaseResponseModel<TicketVerifyModel> model = ResponseUtil.getObjectResponse(String.valueOf(response), TicketVerifyModel.class);
                        if ("200".equals(model.getStatus())) {//成功
                            Log.i(TAG, "onResponse: 成功");
                            ((MainActivity) getActivity()).print(String.valueOf(response), 2);
                            Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {//失败
                            if (mCustomDialog != null && mCustomDialog.isShowing()) {
                                mCustomDialog.dismiss();
                            }
                            mTV_Print.setEnabled(true);
                            mTV_Print.setText("打印最后一次消费");
                            Log.i(TAG, "onResponse: 失败---" + model.getStatus() + "," + model.getMessage());
                            Toast.makeText(getActivity(), model.getMessage(), Toast.LENGTH_SHORT).show();
                        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PrintEvent event) {
        Log.i(TAG, "onMessageEvent: ");
        if (mCustomDialog != null && mCustomDialog.isShowing()) {
            mCustomDialog.dismiss();
        }
        mTV_Print.setEnabled(true);
        mTV_Print.setText("打印最后一次消费");
        switch (event.getFalg()) {
            case 0:
                break;
            case 1:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BackEvent event) {
        switch (event.getFragment()) {
            case 0:
                if (getActivity() != null) {
                    getActivity().finish();
                }
                break;
        }
    }
}
