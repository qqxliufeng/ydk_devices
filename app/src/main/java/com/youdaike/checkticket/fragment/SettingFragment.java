package com.youdaike.checkticket.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youdaike.checkticket.R;
import com.youdaike.checkticket.activity.LoginActivity;
import com.youdaike.checkticket.activity.MainActivity;
import com.youdaike.checkticket.contract.StringContract;
import com.youdaike.checkticket.model.BackEvent;
import com.youdaike.checkticket.utils.Constants;
import com.youdaike.checkticket.utils.PreferencesUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment{

    @BindView(R.id.setting_camera_id)
    TextView mTvCamreId;
    @BindView(R.id.setting_print_count_num)
    TextView mTvPrintCountNum;

    private final String[] items = {"前置摄像头", "后置摄像头", "暂不选择"};
    private final String[] numItems = {"1", "2", "3"};

    public static final int MAX_PRINT_COUNT = 3;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, mRootView);
        initListener();
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int which = PreferencesUtil.getInt(getActivity(),Constants.PreferencesTag.SCANNER_CAMERA,-1);
        switch (which){
            case -1:
                mTvCamreId.setText("选择");
                break;
            case Constants.Scanner.CAMERA_FRONT:
                mTvCamreId.setText("前置");
                break;
            case Constants.Scanner.CAMERA_BACK:
                mTvCamreId.setText("后置");
                break;
        }
        mTvPrintCountNum.setText(String.valueOf(PreferencesUtil.getInt(getActivity(),Constants.PreferencesTag.PRINT_NUM,1)));
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

    @OnClick({R.id.setting_camera_select,R.id.setting_print_count,R.id.mBtSettingLogout})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.setting_camera_select:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                PreferencesUtil.putInt(getActivity(), Constants.PreferencesTag.SCANNER_CAMERA, Constants.Scanner.CAMERA_FRONT);
                                break;
                            case 1:
                                PreferencesUtil.putInt(getActivity(), Constants.PreferencesTag.SCANNER_CAMERA, Constants.Scanner.CAMERA_BACK);
                                break;
                            case 2:
                                PreferencesUtil.putInt(getActivity(), Constants.PreferencesTag.SCANNER_CAMERA,-1);
                                break;
                        }
                        mTvCamreId.setText(items[which]);
                    }
                });
                builder.create().show();
                break;
            case R.id.setting_print_count:
                AlertDialog.Builder numBuilder = new AlertDialog.Builder(getActivity());
                numBuilder.setItems(numItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTvPrintCountNum.setText(numItems[which]);
                        PreferencesUtil.putInt(getActivity(), Constants.PreferencesTag.PRINT_NUM, Integer.parseInt(numItems[which]));
                    }
                });
                numBuilder.create().show();
                break;
            case R.id.mBtSettingLogout:
                AlertDialog.Builder logoutBuilder = new AlertDialog.Builder(getActivity());
                logoutBuilder.setTitle("提示");
                logoutBuilder.setMessage("是否要退出登录并清空所有缓存信息？");
                logoutBuilder.setNegativeButton("取消",null);
                logoutBuilder.setPositiveButton("退出登录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PreferencesUtil.putString(getActivity(), StringContract.PASSWORD, "");
                        getActivity().startActivity(new Intent(getActivity(),LoginActivity.class));
                        EventBus.getDefault().post(new BackEvent(0));
                        getActivity().finish();
                    }
                });
                logoutBuilder.create().show();
                break;
        }
    }


    private void initListener() {
        ((MainActivity) getActivity()).setOnKeyPressedListener(new MainActivity.OnKeyPressedListener() {
            @Override
            public void onKeyPressed(int keycode) {
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BackEvent event) {
        switch (event.getFragment()){
            case 6:
                if (getActivity() != null) {
                    getActivity().finish();
                }
                break;
        }
    }

}
