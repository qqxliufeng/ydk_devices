package com.youdaike.checkticket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.youdaike.checkticket.R;
import com.youdaike.checkticket.contract.StringContract;
import com.youdaike.checkticket.contract.UrlContract;
import com.youdaike.checkticket.model.BaseResponseModel;
import com.youdaike.checkticket.utils.PreferencesUtil;
import com.youdaike.checkticket.utils.ResponseUtil;
import com.youdaike.checkticket.view.CustomDialog;
import com.youdaike.checkticket.view.NumberKeyboardView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    private StringBuilder mPassword;
    private CustomDialog mCustomDialog;

    @BindView(R.id.view_title_bar_title)
    TextView mTV_Title;
    @BindView(R.id.view_title_bar_name)
    TextView mTV_Name;
    @BindView(R.id.activity_login_number_1)
    TextView mTV_Number_1;
    @BindView(R.id.activity_login_number_2)
    TextView mTV_Number_2;
    @BindView(R.id.activity_login_number_3)
    TextView mTV_Number_3;
    @BindView(R.id.activity_login_number_4)
    TextView mTV_Number_4;
    @BindView(R.id.activity_login_number_5)
    TextView mTV_Number_5;
    @BindView(R.id.activity_login_number_6)
    TextView mTV_Number_6;
    @BindView(R.id.activity_login_keyboard)
    NumberKeyboardView mNKV_Keyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mPassword = new StringBuilder("");
        initKeyboardListener();
        mTV_Title.setText("验证密码");
        mTV_Name.setText("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        String password = PreferencesUtil.getString(this, StringContract.PASSWORD);
        Log.i(TAG, "onResume: password=" + password);
        if (!TextUtils.isEmpty(password) && password.length() == 6) {
            for (int i = 0; i < password.length(); i++) {
                inputPWD(password.substring(i, i + 1));
            }
        }
    }

    @OnClick({R.id.view_title_bar_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_title_bar_title_back:
                finish();
                break;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mTV_Number_1.setText("*");
                    break;
                case 2:
                    mTV_Number_2.setText("*");
                    break;
                case 3:
                    mTV_Number_3.setText("*");
                    break;
                case 4:
                    mTV_Number_4.setText("*");
                    break;
                case 5:
                    mTV_Number_5.setText("*");
                    break;
                case 6:
                    mTV_Number_6.setText("*");
                    break;
            }
        }
    };

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_0:
                    inputPWD("0");
                    break;
                case KeyEvent.KEYCODE_1:
                    inputPWD("1");
                    break;
                case KeyEvent.KEYCODE_2:
                    inputPWD("2");
                    break;
                case KeyEvent.KEYCODE_3:
                    inputPWD("3");
                    break;
                case KeyEvent.KEYCODE_4:
                    inputPWD("4");
                    break;
                case KeyEvent.KEYCODE_5:
                    inputPWD("5");
                    break;
                case KeyEvent.KEYCODE_6:
                    inputPWD("6");
                    break;
                case KeyEvent.KEYCODE_7:
                    inputPWD("7");
                    break;
                case KeyEvent.KEYCODE_8:
                    inputPWD("8");
                    break;
                case KeyEvent.KEYCODE_9:
                    inputPWD("9");
                    break;
                case KeyEvent.KEYCODE_BACK:
                    return super.dispatchKeyEvent(event);
                case KeyEvent.KEYCODE_DEL:
                    delPWD();
                    break;
                default:
                    break;
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private void initKeyboardListener() {
        mNKV_Keyboard.setOnKeyboardPressedListener(new NumberKeyboardView.OnKeyboardPressedListener() {
            @Override
            public void onKeyboardPressed(int key) {
                switch (key) {
                    case R.id.view_keyboard_1:
                        inputPWD("1");
                        break;
                    case R.id.view_keyboard_2:
                        inputPWD("2");
                        break;
                    case R.id.view_keyboard_3:
                        inputPWD("3");
                        break;
                    case R.id.view_keyboard_4:
                        inputPWD("4");
                        break;
                    case R.id.view_keyboard_5:
                        inputPWD("5");
                        break;
                    case R.id.view_keyboard_6:
                        inputPWD("6");
                        break;
                    case R.id.view_keyboard_7:
                        inputPWD("7");
                        break;
                    case R.id.view_keyboard_8:
                        inputPWD("8");
                        break;
                    case R.id.view_keyboard_9:
                        inputPWD("9");
                        break;
                    case R.id.view_keyboard_star:
                        break;
                    case R.id.view_keyboard_0:
                        inputPWD("0");
                        break;
                    case R.id.view_keyboard_clear:
                        clearPWD();
                        break;
                    case R.id.view_keyboard_right_back:
                        finish();
                        break;
                    case R.id.view_keyboard_right_delete:
                        delPWD();
                        break;
                    case R.id.view_keyboard_right_submit:
                        if (!TextUtils.isEmpty(mPassword.toString()) && mPassword.toString().length() == 6) {
                            virifyPwd();
                        } else {
                            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }

    private void inputPWD(String number) {
        switch (mPassword.length()) {
            case 0:
                mPassword.append(number);
                mTV_Number_1.setText(number);
                mHandler.sendEmptyMessageDelayed(1, 200);
                break;
            case 1:
                mPassword.append(number);
                mTV_Number_2.setText(number);
                mHandler.sendEmptyMessageDelayed(2, 200);
                break;
            case 2:
                mPassword.append(number);
                mTV_Number_3.setText(number);
                mHandler.sendEmptyMessageDelayed(3, 200);
                break;
            case 3:
                mPassword.append(number);
                mTV_Number_4.setText(number);
                mHandler.sendEmptyMessageDelayed(4, 200);
                break;
            case 4:
                mPassword.append(number);
                mTV_Number_5.setText(number);
                mHandler.sendEmptyMessageDelayed(5, 200);
                break;
            case 5:
                mPassword.append(number);
                mTV_Number_6.setText(number);
                mHandler.sendEmptyMessageDelayed(6, 200);
                virifyPwd();
                break;
        }
    }

    private void delPWD() {
        switch (mPassword.length()) {
            case 0:
                break;
            case 1:
                mPassword.deleteCharAt(0);
                mTV_Number_1.setText("");
                break;
            case 2:
                mPassword.deleteCharAt(1);
                mTV_Number_2.setText("");
                break;
            case 3:
                mPassword.deleteCharAt(2);
                mTV_Number_3.setText("");
                break;
            case 4:
                mPassword.deleteCharAt(3);
                mTV_Number_4.setText("");
                break;
            case 5:
                mPassword.deleteCharAt(4);
                mTV_Number_5.setText("");
                break;
            case 6:
                mPassword.deleteCharAt(5);
                mTV_Number_6.setText("");
                break;
        }
    }

    private void clearPWD() {
        if (mPassword.length() > 0) {
            mPassword.delete(0, mPassword.length());
            mTV_Number_1.setText("");
            mTV_Number_2.setText("");
            mTV_Number_3.setText("");
            mTV_Number_4.setText("");
            mTV_Number_5.setText("");
            mTV_Number_6.setText("");
        }
    }

    private void virifyPwd() {
        Log.i(TAG, "virifyPwd: =" + UrlContract.LOGIN_VERIFY + "?devicesid=" + getIMEI() + "&password=" + mPassword.toString());
        mCustomDialog = CustomDialog.show(this, "验证中···");
        OkHttpUtils
                .post()
                .url(UrlContract.LOGIN_VERIFY)
                .addParams("devicesid", getIMEI())
                .addParams("password", mPassword.toString())
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
                        Toast.makeText(LoginActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.i(TAG, "onResponse: response=" + response);
                        if (mCustomDialog != null && mCustomDialog.isShowing()) {
                            mCustomDialog.dismiss();
                        }
                        BaseResponseModel<String> model = ResponseUtil.getStringResponse(String.valueOf(response));
                        if ("200".equals(model.getStatus())) {//成功
                            Log.i(TAG, "onResponse: 成功");
                            PreferencesUtil.putString(LoginActivity.this, StringContract.PASSWORD, mPassword.toString());
                            enterHome();
                        } else {//失败
                            Log.i(TAG, "onResponse: 失败---" + model.getStatus() + "," + model.getMessage());
                            Toast.makeText(LoginActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void enterHome() {
        Log.i(TAG, "enterHome: pwd=" + mPassword.toString());
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
