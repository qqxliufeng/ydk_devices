package cn.eas.national.deviceapisample.activity.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;

import cn.eas.national.deviceapisample.R;

/**
 * Base Activity.
 * Each code sample activity extends it.
 * @author caizl
 *
 */
public abstract class BaseActivity extends Activity {
    private TextView tvDescriptionTitle, tvDescription, tvDescriptionSeperator, tvInfo;
    private Button btnClearMsg;
    private ScrollView scrollView;

    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isDeviceServiceLogined = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected void initView() {
        this.tvDescriptionTitle = (TextView) findViewById(R.id.tvDescriptionTitle);
        this.tvDescription = (TextView) findViewById(R.id.tvDescription);
        this.tvDescriptionSeperator = (TextView) findViewById(R.id.tvDescriptionSeperator);
        this.scrollView = (ScrollView) findViewById(R.id.scrollView);
        this.tvInfo = (TextView) findViewById(R.id.info_text);
        this.btnClearMsg = (Button) findViewById(R.id.btnClearMsg);
        if (btnClearMsg != null) {
            this.btnClearMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tvInfo.setText("");
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setModuleDescription();
    }

    protected boolean isDeviceServiceLogined() {
        return isDeviceServiceLogined;
    }

    /**
     * Run something on ui thread after milliseconds.
     *
     * @param r
     * @param delayMillis
     */
    public void runOnUiThreadDelayed(Runnable r, int delayMillis) {
        handler.postDelayed(r, delayMillis);
    }

    /**
     * To gain control of the device service,
     * you need invoke this method before any device operation.
     */
    public void bindDeviceService() {
        try {
            isDeviceServiceLogined = false;
            DeviceService.login(this);
            isDeviceServiceLogined = true;
        } catch (RequestException e) {
            // Rebind after a few milliseconds,
            // If you want this application keep the right of the device service
//			runOnUiThreadDelayed(new Runnable() {
//				@Override
//				public void run() {
//					bindDeviceService();
//				}
//			}, 300);
            e.printStackTrace();
        } catch (ServiceOccupiedException e) {
            e.printStackTrace();
        } catch (ReloginException e) {
            e.printStackTrace();
        } catch (UnsupportMultiProcess e) {
            e.printStackTrace();
        }
    }

    /**
     * Release the right of using the device.
     */
    public void unbindDeviceService() {
        DeviceService.logout();
        isDeviceServiceLogined = false;
    }

    /**
     * Get handler in the ui thread
     *
     * @return
     */
    public Handler getUIHandler() {
        return handler;
    }

    protected String getModel() {
        return Build.MODEL;
    }

    /**
     * All device operation result infomation will be displayed by this method.
     *
     * @param info
     */
    protected void displayInfo(final String info) {
        // 将显示的消息滚动到最底部
        getUIHandler().post(new Runnable() {
            @Override
            public void run() {
                String text = tvInfo.getText().toString();
                if(text.isEmpty()) {
                    tvInfo.setText(info);
                } else {
                    tvInfo.setText(text + "\n" + info);
                }
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void setModuleDescription() {
        String desc = getModuleDescription();
        if (tvDescription != null && !TextUtils.isEmpty(desc)) {
            tvDescription.setText(desc);
        } else {
            tvDescriptionTitle.setVisibility(View.GONE);
            tvDescription.setVisibility(View.GONE);
            tvDescriptionSeperator.setVisibility(View.GONE);
        }
    }

    public abstract String getModuleDescription();
}
