package com.youdaike.checkticket.fragment;

import android.app.Fragment;
import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by yuanxx on 2016/9/25.
 */
public class BaseFragment extends Fragment {
    /**
     * 获取设备IMEI
     * @return
     */
    protected String getIMEI() {
        TelephonyManager manager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getDeviceId();
    }
}
