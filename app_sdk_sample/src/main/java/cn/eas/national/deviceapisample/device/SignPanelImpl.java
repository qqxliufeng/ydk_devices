package cn.eas.national.deviceapisample.device;

import android.util.Log;

import com.landicorp.android.eptapi.device.SignPanel;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.BytesBuffer;
import com.landicorp.android.eptapi.utils.IntegerBuffer;

import cn.eas.national.deviceapisample.util.ByteUtil;

import static android.content.ContentValues.TAG;

/**
 * 支持外接签名板（如S160键盘）且主控版本及签名板驱动模块达到要求的终端。
 */

public abstract class SignPanelImpl extends BaseDevice {

    private SignPanel signPanel;

    public SignPanelImpl() {
        this.signPanel = SignPanel.getInstance("USBH");
    }

    public void startSign() {
        boolean result = signPanel.openDevice();
        if (!result) {
            displayInfo("签名板打开失败");
            return;
        }
        SignPanel.OnSignListener onSignListener = new SignPanel.OnSignListener() {

            public void onCrash() {
                displayInfo("签名板服务异常");
            }

            @Override
            public void onSigned() {
                Log.w(TAG, "/// startSign | onSigned");
                BytesBuffer signData = new BytesBuffer();
                IntegerBuffer seqNum = new IntegerBuffer();
                boolean result = signPanel.getSignData(signData, seqNum);
                if (result) {
                    displayInfo("签名数据获取成功，签名数据如下：");
                    displayInfo(ByteUtil.bytes2HexString(signData.getData()));
                } else {
                    displayInfo("签名数据获取失败");
                }
                exitSign();
            }

            @Override
            public void onCancel() {
                exitSign();
                displayInfo("取消签名");
            }

            @Override
            public void onTimeout() {
                exitSign();
                displayInfo("签名超时");
            }

            @Override
            public void onCmdResp(int cmd, int state) {
                exitSign();
                displayInfo("其他签名指令响应：cmd = " + cmd + ", state = " + state);
            }

            @Override
            public void onDisconnected() {
                exitSign();
                displayInfo("签名板断开连接");
            }

            @Override
            public void onFail(int error) {
                exitSign();
                displayInfo("签名失败，error = " + error);
            }
        };
        try {
            // pecialCode（特征码）不能大于8字节
            signPanel.startSign("12345678", 60, onSignListener);
        } catch (RequestException e) {
            e.printStackTrace();
            displayInfo("请求签名板服务异常");
        }
    }

    private void exitSign() {
        signPanel.endSign();
        signPanel.closeDevice();
    }
}
