package com.youdaike.checkticket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.youdaike.checkticket.R;
import com.youdaike.checkticket.contract.UrlContract;
import com.youdaike.checkticket.model.BackEvent;
import com.youdaike.checkticket.model.BaseResponseModel;
import com.youdaike.checkticket.model.VersionModel;
import com.youdaike.checkticket.utils.ResponseUtil;
import com.youdaike.checkticket.utils.VersionUtil;
import com.youdaike.checkticket.view.CustomDialog;
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

public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";

    private CustomDialog mCustomDialog;
    private VersionUtil mVersionUtil;

    @BindView(R.id.view_title_bar_title)
    TextView mTV_Title;
    @BindView(R.id.view_title_bar_name)
    TextView mTV_Name;
    @BindView(R.id.home_verify)
    LinearLayout mLL_Verify;
    @BindView(R.id.home_query)
    LinearLayout mLL_Query;
    @BindView(R.id.home_report)
    LinearLayout mLL_Report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mTV_Title.setText("优待客电子票核销平台");
        mTV_Name.setText("");
        mVersionUtil = new VersionUtil(this);
        EventBus.getDefault().register(this);
        checkVersion();
    }

    @OnClick({R.id.home_verify, R.id.home_query, R.id.home_report,R.id.home_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_verify:
                enterMain(0);
                break;
            case R.id.home_query:
                enterMain(1);
                break;
            case R.id.home_report:
                enterMain(3);
                break;
            case R.id.home_setting:
                enterMain(6);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackEvent(BackEvent backEvent){
        finish();
    }


    private void enterMain(int from) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("FROM", from);
        startActivity(intent);
    }

    /**
     * 检查版本
     */
    private void checkVersion() {
        Log.i(TAG, "checkVersion: =" + UrlContract.CHECK_VERSION + "?devicesid=" + getIMEI());
        mCustomDialog = CustomDialog.show(this, "检查版本···");
        OkHttpUtils
                .post()
                .url(UrlContract.CHECK_VERSION)
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
                        Toast.makeText(HomeActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.i(TAG, "onResponse: response=" + response);
                        if (mCustomDialog != null && mCustomDialog.isShowing()) {
                            mCustomDialog.dismiss();
                        }
                        BaseResponseModel<VersionModel> model = ResponseUtil.getObjectResponse(String.valueOf(response), VersionModel.class);
                        if ("200".equals(model.getStatus())) {//成功
                            Log.i(TAG, "onResponse: 成功");
                            mVersionUtil.showNoticeDialog(Integer.valueOf(model.getData().getVersion()), UrlContract.BASE_URL + model.getData().getAppurl(), model.getData().getIsreset(), null);
                        } else {//失败
                            Log.i(TAG, "onResponse: 失败---" + model.getStatus() + "," + model.getMessage());
                            Toast.makeText(HomeActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK){
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }
}
