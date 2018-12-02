package com.youdaike.checkticket.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.landicorp.android.eptapi.DeviceService;
import com.landicorp.android.eptapi.device.Printer;
import com.landicorp.android.eptapi.exception.ReloginException;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.exception.ServiceOccupiedException;
import com.landicorp.android.eptapi.exception.UnsupportMultiProcess;
import com.youdaike.checkticket.R;
import com.youdaike.checkticket.contract.StringContract;
import com.youdaike.checkticket.contract.UrlContract;
import com.youdaike.checkticket.fragment.HistoryFragment;
import com.youdaike.checkticket.fragment.QueryDetailFragment;
import com.youdaike.checkticket.fragment.QueryFragment;
import com.youdaike.checkticket.fragment.ReportFragment;
import com.youdaike.checkticket.fragment.VerifyDetailFragment;
import com.youdaike.checkticket.fragment.VerifyFragment;
import com.youdaike.checkticket.model.BackEvent;
import com.youdaike.checkticket.model.BaseResponseModel;
import com.youdaike.checkticket.model.PrintEvent;
import com.youdaike.checkticket.model.ScenicNameModel;
import com.youdaike.checkticket.utils.PreferencesUtil;
import com.youdaike.checkticket.utils.ResponseUtil;
import com.youdaike.checkticket.utils.liandi.impl.print.PrinterImpl;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hdx.HdxUtil;
import okhttp3.Call;
import okhttp3.Response;

public class MainActivity extends SerialPortActivity {
    private static final String TAG = "MainActivity";

    private int mFrom;
    private MainActivity.OnKeyPressedListener mOnKeyPressedListener;
    private FragmentManager mFragmentManager;
    private VerifyFragment mVerifyFragment;
    private QueryFragment mQueryFragment;
    private HistoryFragment mHistoryFragment;
    private ReportFragment mReportFragment;

    private VerifyDetailFragment mVerifyDetailFragment;
    private QueryDetailFragment mQueryDetailFragment;

    @BindView(R.id.view_title_bar_title)
    TextView mTV_Title;
    @BindView(R.id.view_title_bar_name)
    TextView mTV_Name;
    @BindView(R.id.main_container)
    FrameLayout mContainer;

    private final int ENABLE_BUTTON = 2;
    private final int SHOW_VERSION = 3;
    private final int UPDATE_FW = 4;
    private final int SHOW_PROGRESS = 5;
    private final int DISABLE_BUTTON = 6;
    private final int HIDE_PROGRESS = 7;
    private final int REFRESH_PROGRESS = 8;
    private final int SHOW_FONT_UPTAE_INFO = 9;
    private final int SHOW_PRINTER_INFO_WHEN_INIT = 10;
    private final byte HDX_ST_NO_PAPER1 = (byte) (1 << 0);     // 1 缺纸
    //private final byte  HDX_ST_BUF_FULL  = (byte)(1<<1);     // 1 缓冲满
    //private final byte  HDX_ST_CUT_ERR   = (byte)(1<<2);     // 1 打印机切刀错误
    private final byte HDX_ST_HOT = (byte) (1 << 4);     // 1 打印机太热
    private final byte HDX_ST_WORK = (byte) (1 << 5);     // 1 打印机在工作状态

    private boolean stop = false;
    public static boolean ver_start_falg = false;
    boolean Status_Start_Falg = false;
    byte[] Status_Buffer = new byte[300];
    int Status_Buffer_Index = 0;
    public static StringBuilder strVer = new StringBuilder("922");
    public static StringBuilder oldVer = new StringBuilder("922");
    private static String Error_State = "";
    Time time = new Time();
    int TimeSecond;
    private int iProgress = 0;
    String Printer_Info = new String();

    public static boolean flow_start_falg = false;
    byte[] flow_buffer = new byte[300];

    public TextView TextViewSerialRx;
    public static Context context;
    MyHandler handler;
    CustomHandler mCustomHandler;

    WakeLock lock;
    int printer_status = 0;
    private ProgressDialog m_pDialog;

    private PrinterImpl printer;

    private class MyHandler extends Handler {
        public void handleMessage(Message msg) {
            if (stop == true)
                return;
            switch (msg.what) {
                case DISABLE_BUTTON:
                    Close_Button();
                    Log.d(TAG, "DISABLE_BUTTON");
                    break;
                case ENABLE_BUTTON:
                    //ButtonUpdateVersion.setEnabled(true);
                    //ButtonUpdateFontLib.setEnabled(true);
                    Log.d(TAG, "ENABLE_BUTTON");
                    break;
                case SHOW_FONT_UPTAE_INFO:
                    TextView tv3 = new TextView(MainActivity.this);
                    tv3.setText((String) msg.obj);
                    tv3.setGravity(Gravity.CENTER);
                    tv3.setTextSize(25);
                    tv3.findFocus();
                    new AlertDialog.Builder(MainActivity.this)
                            .setIcon(R.drawable.logo)
                            .setView(tv3)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    handler.sendMessage(handler.obtainMessage(ENABLE_BUTTON, 1, 0, null));
                                }
                            }).show();
                    break;
                case SHOW_VERSION:
                    TextView tv2 = new TextView(MainActivity.this);
                    tv2.setText(getString(R.string.currentFWV)
                            + MainActivity.strVer.toString());
                    tv2.setGravity(Gravity.CENTER);
                    tv2.setTextSize(25);
                    tv2.findFocus();
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(getString(R.string.getV))
                            .setIcon(R.drawable.logo)
                            .setView(tv2)
                            .setCancelable(false)
                            .setPositiveButton("OK", null).show();
                    break;
                case UPDATE_FW:
                    m_pDialog.hide();
                    TextView tv4 = new TextView(MainActivity.this);
                    // if(!MainActivity.oldVer.toString().isEmpty())
                {
                    tv4.setText(getString(R.string.previousFWV)
                            + MainActivity.oldVer.toString() + "\n"
                            + getString(R.string.currentFWV)
                            + MainActivity.strVer.toString());
                    TextViewSerialRx.setText(Printer_Info
                            + getString(R.string.previousFWV)
                            + MainActivity.oldVer.toString() + "\n"
                            + getString(R.string.currentFWV)
                            + MainActivity.strVer.toString());
                }
                // else
                {
                    // tv3.setText("update firmware version failed ");
                }
                tv4.setGravity(Gravity.CENTER);
                tv4.setTextSize(22);
                tv4.findFocus();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.updateFWFinish))
                        .setIcon(R.drawable.logo).setView(tv4)
                        .setCancelable(false)
                        .setPositiveButton("OK", null).show();
                break;
                case SHOW_PROGRESS:
                    m_pDialog = new ProgressDialog(MainActivity.this);
                    m_pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    m_pDialog.setMessage((String) msg.obj);
                    m_pDialog.setIndeterminate(false);
                    m_pDialog.setCancelable(false);
                    m_pDialog.show();
                    break;
                case HIDE_PROGRESS:
                    m_pDialog.hide();
                    break;
                case REFRESH_PROGRESS:
                    m_pDialog.setProgress(iProgress);
                    break;
                case SHOW_PRINTER_INFO_WHEN_INIT:
                    TextViewSerialRx.setText(Printer_Info + strVer.toString());
                    break;
                default:
                    break;
            }
        }
    }

    private class CustomHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    EventBus.getDefault().post(new PrintEvent(1));
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        printer = new PrinterImpl(this) {

            @Override
            protected void onDeviceServiceCrash() {
                mCustomHandler.sendEmptyMessage(0);//隐藏loading
            }

            @Override
            protected void displayInfo(String info) {
                Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();
                mCustomHandler.sendEmptyMessage(0);//隐藏loading
            }

            @Override
            protected void toast(String msg) {
            }
        };
        handler = new MyHandler();
        mCustomHandler = new CustomHandler();
        ButterKnife.bind(this);
        mFrom = getIntent().getIntExtra("FROM", 0);
        mFragmentManager = getFragmentManager();
        setFragment(mFrom, null);
        if (PreferencesUtil.getBoolean(this, StringContract.ISHDXDEVICE, false)) {
            HdxUtil.SwitchSerialFunction(HdxUtil.SERIAL_FUNCTION_PRINTER);
        }
        PowerManager pm = (PowerManager) getApplicationContext()
                .getSystemService(Context.POWER_SERVICE);
        lock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, TAG);
        MainActivity.strVer = new StringBuilder();
        MainActivity.oldVer = new StringBuilder();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i(TAG, "onConfigurationChanged: ORIENTATION_LANDSCAPE");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.i(TAG, "onConfigurationChanged: ORIENTATION_PORTRAIT");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        getScenicName();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.view_title_bar_title_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_title_bar_title_back:
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.main_container);
                int tag = Integer.valueOf(fragment.getTag());
                Log.i(TAG, "tag=" + tag);
                EventBus.getDefault().post(new BackEvent(tag));
                break;
        }
    }

    public void setFragment(int index, Bundle bundle) {
        Log.d(TAG, "setFragment() called with: " + "index = [" + index + "]");
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        switch (index) {
            case 0:
                mTV_Title.setText("凭证验证");
                mVerifyFragment = new VerifyFragment();
                transaction.replace(R.id.main_container, mVerifyFragment, "0");
                break;
            case 1:
                mTV_Title.setText("凭证查询");
                mQueryFragment = new QueryFragment();
                transaction.replace(R.id.main_container, mQueryFragment, "1");
                break;
            case 2:
                mTV_Title.setText("交易历史");
                mHistoryFragment = new HistoryFragment();
                transaction.replace(R.id.main_container, mHistoryFragment, "2");
                break;
            case 3:
                mTV_Title.setText("统计报表");
                mReportFragment = new ReportFragment();
                transaction.replace(R.id.main_container, mReportFragment, "3");
                break;
            case 4:
                mTV_Title.setText("凭证验证");
                mVerifyDetailFragment = new VerifyDetailFragment();
                mVerifyDetailFragment.setArguments(bundle);
                transaction.replace(R.id.main_container, mVerifyDetailFragment, "4");
                break;
            case 5:
                mTV_Title.setText("凭证查询");
                mQueryDetailFragment = new QueryDetailFragment();
                mQueryDetailFragment.setArguments(bundle);
                transaction.replace(R.id.main_container, mQueryDetailFragment, "5");
                break;
        }
        transaction.addToBackStack(null).commit();
    }

    private void getScenicName() {
        Log.i(TAG, "getScenicName: =" + UrlContract.GET_SCENIC_NAME + "?devicesid=" + getIMEI());
        OkHttpUtils
                .post()
                .url(UrlContract.GET_SCENIC_NAME)
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
                        Toast.makeText(MainActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.i(TAG, "onResponse: response=" + response);
                        BaseResponseModel<ScenicNameModel> model = ResponseUtil.getObjectResponse(String.valueOf(response), ScenicNameModel.class);
                        if ("200".equals(model.getStatus())) {//成功
                            Log.i(TAG, "onResponse: 成功---" + model.getData().getTitle());
                            if (!TextUtils.isEmpty(model.getData().getTitle())) {
                                mTV_Name.setText(model.getData().getTitle());
                                PreferencesUtil.putString(MainActivity.this, StringContract.SCENICNAME, model.getData().getTitle());
                            }
                        } else {//失败
                            Log.i(TAG, "onResponse: 失败---" + model.getStatus() + "," + model.getMessage());
                            Toast.makeText(MainActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            DeviceService.login(this);
        } catch (ServiceOccupiedException e) {
            e.printStackTrace();
        } catch (ReloginException e) {
            e.printStackTrace();
        } catch (UnsupportMultiProcess unsupportMultiProcess) {
            unsupportMultiProcess.printStackTrace();
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }


    /**
     * 打印
     *
     * @param json
     * @param type
     */
    public void print(String json, int type) {
        int ret = printer.getPrinterStatus();
        if (ret != Printer.ERROR_NONE) {
            Toast.makeText(this, printer.getDescribe(ret), Toast.LENGTH_SHORT).show();
            return;
        }
        printer.init();
        startPrint(json, type);
    }

    private void startPrint(String json, int type) {
        printer.init();
        switch (type) {
            case 1://验证时打印【商家凭证】
                if (!printer.addTextType1(json)) {
                    Toast.makeText(context, "打印失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                addEndPrint();
                break;
            case 2://打印最后一次消费【重复打印】
                if (!printer.addTextType2(json)) {
                    Toast.makeText(context, "打印失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                addEndPrint();
                break;
            case 3://凭证查询打印【凭证查询】
                break;
            case 4://统计报表
                if (!printer.addTextType4(json)) {
                    Toast.makeText(context, "打印失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                addEndPrint();
                break;
            case 5://凭证查询打印【凭证查询】
                if (!printer.addTextType5(json)) {
                    Toast.makeText(context, "打印失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                addEndPrint();
                break;
        }
    }

    private void addEndPrint() {
        if (!printer.feedLine(3)) {
            Toast.makeText(context, "打印失败", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!printer.cutPaper()) {
            Toast.makeText(context, "打印失败", Toast.LENGTH_SHORT).show();
            return;
        }
        printer.startPrint();
    }


    public boolean getPrintStatue() {
        int ret = printer.getPrinterStatus();
        if (ret != Printer.ERROR_NONE) {
            Toast.makeText(this, printer.getDescribe(ret), Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (mOnKeyPressedListener != null) {
                mOnKeyPressedListener.onKeyPressed(event.getKeyCode());
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public interface OnKeyPressedListener {
        void onKeyPressed(int keycode);
    }

    public void setOnKeyPressedListener(MainActivity.OnKeyPressedListener listener) {
        mOnKeyPressedListener = listener;
    }

    @Override
    protected void onDataReceived(final byte[] buffer, final int size, final int n) {
        int i;
        String strTemp;
        if (Status_Start_Falg == true) {
            for (i = 0; i < size; i++) {
                Status_Buffer[getStatus_Buffer_Index()] = buffer[i];
                setStatus_Buffer_Index(1 + i);
            }
        }

        if (MainActivity.ver_start_falg == true) {
            for (i = 0; i < size; i++) {
                MainActivity.strVer.append(String.format("%c", (char) buffer[i]));
            }

        }
        /*
         * 	public static boolean flow_start_falg = false;
		byte [] flow_buffer=new byte[300];
		
		 * */

        StringBuilder str = new StringBuilder();
        StringBuilder strBuild = new StringBuilder();
        for (i = 0; i < size; i++) {
            if (flow_start_falg == true) {
                if ((buffer[i] == 0x13) || (buffer[i] == 0x11)) {
                    flow_buffer[0] = buffer[i];

                }
            }
            str.append(String.format(" %x", buffer[i]));
            strBuild.append(String.format("%c", (char) buffer[i]));
        }
        Log.e(TAG, "onReceivedC= " + strBuild.toString());
        Log.e(TAG, "onReceivedx= " + str.toString());

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Log.e("TAG", data.getStringExtra("scan decode result text"));
        }
    }

    int getStatus_Buffer_Index() {
        return Status_Buffer_Index;

    }

    void setStatus_Buffer_Index(int v) {
        Status_Buffer_Index = v;
    }

    void Close_Button() {
    }

    byte Get_Printer_Status() {
        Status_Buffer[0] = 0;
        Status_Buffer[1] = 0;
        Status_Start_Falg = true;
        setStatus_Buffer_Index(0);
        sendCommand(0x1b, 0x76);
        Log.i(TAG, "Get_Printer_Status->0x1b,0x76");
        Time_Check_Start();

        while (true) {
            if (getStatus_Buffer_Index() > 0) {

                Status_Start_Falg = false;
                Log.e(TAG, "Get_Printer_Status :" + Status_Buffer[0]);
                return Status_Buffer[0];
            }
            if (TimeIsOver(5)) {
                Status_Start_Falg = false;
                Log.e(TAG, "Get_Printer_Status->TIME OVER:" + Status_Buffer[0]);
                return (byte) 0xff;

            }
            sleep(50);
        }


    }

    void PrinterPowerOnAndWaitReady() {

        //Status_Buffer_Index=0;
        //Status_Start_Falg = true;
        HdxUtil.SetPrinterPower(1);
        sleep(500);
    }

    void PrinterPowerOff() {
        HdxUtil.SetPrinterPower(0);
    }

    //返回真, 有纸, 返回假 没有纸
    boolean Printer_Is_Normal() {
        byte status;


        status = Get_Printer_Status();

        if (status == 0xff) {
            Log.e(TAG, "huck time is out");
            Error_State = "huck unkown err";
            return false;

        }

        if ((status & HDX_ST_NO_PAPER1) > 0) {

            Log.d(TAG, "huck is not paper");
            Error_State = getResources().getString(R.string.IsOutOfPaper);
            return false;
        } else if ((status & HDX_ST_HOT) > 0) {
            Log.d(TAG, "huck is too hot");
            Error_State = getResources().getString(R.string.PrinterNotNormal1);
            return false;
        } else {
            Log.d(TAG, " huck is ready");
            return true;
        }


    }

    //判断打印机装好纸 ,如果有 ,返回真,否者返回假
    boolean Warning_When_Not_Normal() {


        handler.sendMessage(handler.obtainMessage(DISABLE_BUTTON, 1, 0, null));
        if (Printer_Is_Normal()) {

            Log.i(TAG, "quck_Is_Normal ok");
            return true;
        } else {
            handler.sendMessage(handler.obtainMessage(SHOW_FONT_UPTAE_INFO, 1, 0, Error_State));
            Log.d(TAG, " quck_Is not_Paper");
            return false;

        }

    }

    void Time_Check_Start() {
        time.setToNow(); // ȡ��ϵͳʱ�䡣
        TimeSecond = time.second;


    }

    boolean TimeIsOver(int second) {

        time.setToNow(); // ȡ��ϵͳʱ�䡣
        int t = time.second;
        if (t < TimeSecond) {
            t += 60;
        }

        if (t - TimeSecond > second) {
            return true;
        }
        return false;
    }


    private void sendCommand(int... command) {
        try {
            for (int i = 0; i < command.length; i++) {
                mOutputStream.write(command[i]);
                // Log.e(TAG,"command["+i+"] = "+Integer.toHexString(command[i]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // / sleep(1);
    }

    private class PrintThread extends Thread {

        String json;
        int type;

        public PrintThread(String param, int i) {
            json = param;
            type = i;
        }

        @Override
        public void run() {
            super.run();
            PrinterPowerOnAndWaitReady();
//            if (!Warning_When_Not_Normal()) {
//                PrinterPowerOff();
//                EventBus.getDefault().post(new PrintEvent());
//                return;
//            }

            lock.acquire();
            sendPrintCommand(json, type);
        }
    }

    private void sendPrintCommand(String json, int type) {
        if (TextUtils.isEmpty(json)) {
            return;
        }
        Log.i(TAG, "sendPrintCommand(): json = " + json);
        sendCommand(0x1B, 0x23, 0x23, 0x53, 0x4C, 0x41, 0x4E, 0x0f); // china
        sleep(200);
        sendCommand(0x1B, 0x33, 0x14); // 行距
        sendCommand(0x0a);

        switch (type) {
            case 1://验证时打印【商家凭证】
                try {
                    JSONObject jsonObj = JSONObject.fromObject(json);
                    String data = jsonObj.optString("data");
                    if (TextUtils.isEmpty(data)) {
                        return;
                    }
                    //标题
                    sendCommand(0x1B, 0x20, 0x02);//字间距
                    sendCommand(0x1B, 0x61, 0x01);//居中
                    sendCommand(0x1B, 0x45, 0x01);//加粗
                    mOutputStream.write(("优待客票务【商家凭证】").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("-------------------------").getBytes("cp936"));
                    sendCommand(0x1B, 0x45, 0x00);//不加粗
                    sendCommand(0x1B, 0x61, 0x00);//左对齐
                    sendCommand(0x0a);
                    //内容
                    JSONObject dataObj_1 = JSONObject.fromObject(data);
                    Iterator iterator_1 = dataObj_1.keys();
                    sendCommand(0x0a);
                    //内容
                    while (iterator_1.hasNext()) {
                        String key = iterator_1.next().toString();
                        String value = dataObj_1.optString(key);
                        if ("手机".equals(key)) {
                            if (value != null && value.trim().length() == 11) {
                                value = value.trim().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                            }
                        }
                        Log.i(TAG, "sendPrintCommand: =" + key + ":" + value);
                        mOutputStream.write((key + ":" + value).getBytes("cp936"));
                        sendCommand(0x0a);
                    }
                    mOutputStream.write(("-------------------------").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("温馨提示:").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("1.一经打印，不可退改").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("2.验证当天有效，过期作废").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("客服电话:0531-85333222").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("  ").getBytes("cp936"));
                    sendCommand(0x0a);
                    sendCommand(0x1B, 0x64, 0x03);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2://打印最后一次消费【重复打印】
                try {
                    JSONObject jsonObj = JSONObject.fromObject(json);
                    String data = jsonObj.optString("data");
                    if (TextUtils.isEmpty(data)) {
                        return;
                    }
                    //标题
                    sendCommand(0x1B, 0x20, 0x02);//字间距
                    sendCommand(0x1B, 0x61, 0x01);//居中
                    sendCommand(0x1B, 0x45, 0x01);//加粗
                    mOutputStream.write(("优待客票务【重复打印】").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("(请核实此订单是否重复使用)").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("-------------------------").getBytes("cp936"));
                    sendCommand(0x1B, 0x45, 0x00);//不加粗
                    sendCommand(0x1B, 0x61, 0x00);//左对齐
                    sendCommand(0x0a);
                    //内容
                    JSONObject dataObj_3 = JSONObject.fromObject(data);
                    Iterator iterator_3 = dataObj_3.keys();
                    while (iterator_3.hasNext()) {
                        String key = iterator_3.next().toString();
                        String value = dataObj_3.optString(key);
                        Log.i(TAG, "sendPrintCommand: =" + key + ":" + value);
                        mOutputStream.write((key + ":" + value).getBytes("cp936"));
                        sendCommand(0x0a);
                    }
                    mOutputStream.write(("打印时间:" + getTime()).getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("-------------------------").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("温馨提示:").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("1.请核实凭证号是否重复使用").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("2.此凭证相关人员签字后有效").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("客服电话:0531-85333222").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("景区工作人员签字：").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("客户签字：").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("客户电话：").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("  ").getBytes("cp936"));
                    sendCommand(0x0a);
                    sendCommand(0x1B, 0x64, 0x03);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3://凭证查询打印【凭证查询】
                try {
                    JSONObject jsonObj = JSONObject.fromObject(json);
                    String data = jsonObj.optString("data");
                    if (TextUtils.isEmpty(data)) {
                        return;
                    }
                    //标题
                    sendCommand(0x1B, 0x20, 0x02);//字间距
                    sendCommand(0x1B, 0x61, 0x01);//居中
                    sendCommand(0x1B, 0x45, 0x01);//加粗
                    mOutputStream.write(("优待客票务【凭证查询】").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("(此凭证不能作为入园凭证使用)").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("-------------------------").getBytes("cp936"));
                    sendCommand(0x1B, 0x45, 0x00);//不加粗
                    sendCommand(0x1B, 0x61, 0x00);//左对齐
                    sendCommand(0x0a);
                    //内容
                    JSONObject dataObj_4 = JSONObject.fromObject(data);
                    Iterator iterator_4 = dataObj_4.keys();
                    while (iterator_4.hasNext()) {
                        String key = iterator_4.next().toString();
                        String value = dataObj_4.optString(key);
                        Log.i(TAG, "sendPrintCommand: =" + key + ":" + value);
                        if (!"couponlog".equals(key)) {
                            switch (key) {
                                case "title":
                                    key = "票种";
                                    break;
                                case "buynum":
                                    key = "总购次数";
                                    break;
                                case "surplusnm":
                                    key = "可用次数";
                                    break;
                                case "moblie":
                                    key = "客户手机号";
                                    if (value != null && value.trim().length() == 11) {
                                        value = value.trim().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                                    }
                                    break;
                                case "buytime":
                                    key = "购票时间";
                                    break;
                                case "allowdate":
                                    key = "期限";
                                    break;
                            }
                            mOutputStream.write((key + ":" + value).getBytes("cp936"));
                            sendCommand(0x0a);
                        } else {
                            mOutputStream.write(("打印时间:" + getTime()).getBytes("cp936"));
                            sendCommand(0x0a);
                            JSONArray couponArray = dataObj_4.optJSONArray("couponlog");
                            Log.i(TAG, "sendPrintCommand: couponArray=" + couponArray.size());
                            if (couponArray != null && couponArray.size() > 0) {
                                for (int i = 0; i < couponArray.size(); i++) {
                                    JSONObject dataObj = JSONObject.fromObject(couponArray.get(i));
                                    Iterator iterator = dataObj.keys();
                                    //内容
                                    mOutputStream.write(("-------------------------").getBytes("cp936"));
                                    sendCommand(0x0a);
                                    mOutputStream.write(("消费明细").getBytes("cp936"));
                                    sendCommand(0x0a);
                                    while (iterator.hasNext()) {
                                        String k = iterator.next().toString();
                                        String v = dataObj.optString(k);
                                        switch (k) {
                                            case "id":
                                                k = "序号";
                                                break;
                                            case "createtime":
                                                k = "消费时间";
                                                break;
                                            case "couponnum":
                                                k = "消费数量";
                                                break;
                                        }
                                        mOutputStream.write((k + ":" + v).getBytes("cp936"));
                                        sendCommand(0x0a);
                                    }
                                }
                            }
                        }

                    }
                    mOutputStream.write(("  ").getBytes("cp936"));
                    sendCommand(0x0a);
                    sendCommand(0x1B, 0x64, 0x03);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4://统计报表
                try {
                    JSONObject jsonObj = JSONObject.fromObject(json);
                    String data = jsonObj.optString("data");
                    if (TextUtils.isEmpty(data)) {
                        return;
                    }
                    //标题
                    sendCommand(0x1B, 0x20, 0x02);//字间距
                    sendCommand(0x1B, 0x61, 0x01);//居中
                    sendCommand(0x1B, 0x45, 0x01);//加粗
                    mOutputStream.write(("优待客票务【统计报表】").getBytes("cp936"));
                    sendCommand(0x0a);
                    sendCommand(0x1B, 0x45, 0x00);//不加粗
                    sendCommand(0x0a);
                    //内容
                    JSONArray dataArray = jsonObj.optJSONArray("data");
                    if (dataArray != null && dataArray.size() > 0) {
                        for (int i = 0; i < dataArray.size(); i++) {
                            JSONObject dataObj_2 = JSONObject.fromObject(dataArray.get(i));
                            Iterator iterator_2 = dataObj_2.keys();
                            sendCommand(0x1B, 0x61, 0x00);//左对齐
                            mOutputStream.write(("-------------------------").getBytes("cp936"));
                            sendCommand(0x0a);
                            while (iterator_2.hasNext()) {
                                String key = iterator_2.next().toString();
                                String value = dataObj_2.optString(key);
                                Log.i(TAG, "sendPrintCommand: =" + key + ":" + value);
                                switch (key) {
                                    case "date":
                                        key = "时间";
                                        break;
                                    case "title":
                                        key = "名称";
                                        break;
                                    case "num":
                                        key = "数量";
                                        break;
                                }
                                mOutputStream.write((key + ":" + value).getBytes("cp936"));
                                sendCommand(0x0a);
                            }
                            mOutputStream.write(("打印时间:" + getTime()).getBytes("cp936"));
                            sendCommand(0x0a);
                            mOutputStream.write(("  ").getBytes("cp936"));
                            sendCommand(0x0a);
                            sendCommand(0x1B, 0x64, 0x03);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 5://凭证查询打印【凭证查询】
                try {
                    //标题
                    sendCommand(0x1B, 0x20, 0x02);//字间距
                    sendCommand(0x1B, 0x61, 0x01);//居中
                    sendCommand(0x1B, 0x45, 0x01);//加粗
                    mOutputStream.write(("优待客票务【凭证查询】").getBytes("cp936"));
                    sendCommand(0x0a);
                    mOutputStream.write(("(此凭证不能作为入园凭证使用)").getBytes("cp936"));
                    sendCommand(0x1B, 0x45, 0x00);//不加粗
                    sendCommand(0x1B, 0x61, 0x00);//左对齐
                    sendCommand(0x0a);
                    //内容
                    JSONArray dataArray = JSONArray.fromObject(json);
                    if (dataArray != null && dataArray.size() > 0) {
                        for (int i = 0; i < dataArray.size(); i++) {
                            JSONObject dataObj_2 = JSONObject.fromObject(dataArray.get(i));
                            Iterator iterator_2 = dataObj_2.keys();
                            sendCommand(0x1B, 0x61, 0x00);//左对齐
                            mOutputStream.write(("-------------------------").getBytes("cp936"));
                            sendCommand(0x0a);
                            HashMap<Integer, String> sortValue = new HashMap<Integer, String>();//重新排序
                            while (iterator_2.hasNext()) {
                                String key = iterator_2.next().toString();
                                String value = dataObj_2.optString(key);
                                Log.i(TAG, "sendPrintCommand: =" + key + ":" + value);
                                switch (key) {
                                    case "prname":
                                        key = "商家名称";
                                        sortValue.put(0, (key + ":" + value));
                                        break;
                                    case "teamtitle":
                                        key = "产品名称";
                                        sortValue.put(1, (key + ":" + value));
                                        break;
                                    case "fxs":
                                        key = "分销商";
                                        sortValue.put(2, (key + ":" + value));
                                        break;
                                    case "couponno":
                                        key = "凭证号";
                                        sortValue.put(3, (key + ":" + value));
                                        break;
                                    case "couponnum":
                                        key = "消费数量";
                                        sortValue.put(4, (key + ":" + value));
                                        break;
                                    case "orderlink":
                                        key = "联系人";
                                        sortValue.put(5, (key + ":" + value));
                                        break;
                                    case "ordermoblie":
                                        key = "手机";
                                        if (value != null && value.trim().length() == 11) {
                                            value = value.trim().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                                        }
                                        sortValue.put(6, (key + ":" + value));
                                        break;
                                    case "ordertime":
                                        key = "购票时间";
                                        sortValue.put(7, (key + ":" + value));
                                        break;
                                    case "createtime":
                                        key = "消费时间";
                                        sortValue.put(8, (key + ":" + value));
                                        break;
                                    case "teamtotime":
                                        key = "期限";
                                        sortValue.put(9, (key + ":" + value));
                                        break;
                                }
                            }
                            for (int j = 0; j < sortValue.size(); j++) {
                                mOutputStream.write(sortValue.get(j).getBytes("cp936"));
                                sendCommand(0x0a);
                            }
                            mOutputStream.write(("打印时间:" + getTime()).getBytes("cp936"));
                            sendCommand(0x0a);
                        }
                        mOutputStream.write(("温馨提示:").getBytes("cp936"));
                        sendCommand(0x0a);
                        mOutputStream.write(("1.查询结果不能作为入园凭证").getBytes("cp936"));
                        sendCommand(0x0a);
                        mOutputStream.write(("2.特殊情况，请认真核实").getBytes("cp936"));
                        sendCommand(0x0a);
                        mOutputStream.write(("客服电话:0531-85333222").getBytes("cp936"));
                        sendCommand(0x0a);
                        mOutputStream.write(("客户签字").getBytes("cp936"));
                        sendCommand(0x0a);
                        mOutputStream.write(("客户电话").getBytes("cp936"));
                        sendCommand(0x0a);
                        sendCommand(0x1B, 0x64, 0x03);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        EventBus.getDefault().post(new PrintEvent(1));

        /**
         try {
         //标题
         sendCommand(0x1B, 0x20, 0x02);//字间距
         sendCommand(0x1B, 0x61, 0x01);//居中
         sendCommand(0x1B, 0x45, 0x01);//“优待客”加粗
         mOutputStream.write(("优待客").getBytes("cp936"));
         sendCommand(0x1B, 0x45, 0x00);//“票务”正常
         mOutputStream.write(("票务").getBytes("cp936"));
         mOutputStream.write(("").getBytes("cp936"));
         sendCommand(0x0a);
         } catch (IOException e) {
         e.printStackTrace();
         }
         switch (type) {
         case 1://验证时打印
         JSONObject dataObj_1 = JSONObject.fromObject(data);
         Iterator iterator_1 = dataObj_1.keys();
         try {
         mOutputStream.write(("   ").getBytes("cp936"));
         sendCommand(0x0a);
         //内容
         sendCommand(0x1B, 0x61, 0x00);//左对齐
         while (iterator_1.hasNext()) {
         String key = iterator_1.next().toString();
         String value = dataObj_1.optString(key);
         Log.i(TAG, "sendPrintCommand: =" + key + ":" + value);
         mOutputStream.write((key + ":" + value).getBytes("cp936"));
         sendCommand(0x0a);
         }
         mOutputStream.write(("  ").getBytes("cp936"));
         sendCommand(0x0a);
         sendCommand(0x1B, 0x64, 0x03);
         } catch (IOException e) {
         e.printStackTrace();
         }
         break;
         case 2: //交易历史 列表
         JSONArray dataArray = jsonObj.optJSONArray("data");
         if (dataArray != null && dataArray.size() > 0) {
         try {
         mOutputStream.write(("   ").getBytes("cp936"));
         sendCommand(0x0a);
         } catch (Exception e) {
         e.printStackTrace();
         }
         for (int i = 0; i < dataArray.size(); i++) {
         JSONObject dataObj_2 = JSONObject.fromObject(dataArray.get(i));
         Iterator iterator_2 = dataObj_2.keys();
         try {
         //内容
         sendCommand(0x1B, 0x61, 0x00);//左对齐
         mOutputStream.write(("--------------------").getBytes("cp936"));
         sendCommand(0x0a);
         while (iterator_2.hasNext()) {
         String key = iterator_2.next().toString();
         String value = dataObj_2.optString(key);
         Log.i(TAG, "sendPrintCommand: =" + key + ":" + value);
         switch (key) {
         case "date":
         key = "日期";
         break;
         case "title":
         key = "票种";
         break;
         case "num":
         key = "次数";
         break;
         }
         mOutputStream.write((key + ":" + value).getBytes("cp936"));
         sendCommand(0x0a);
         }
         mOutputStream.write(("  ").getBytes("cp936"));
         sendCommand(0x0a);
         sendCommand(0x1B, 0x64, 0x03);
         } catch (Exception e) {
         e.printStackTrace();
         }
         }
         }
         break;
         case 3: //打印最后一次消费
         JSONObject dataObj_3 = JSONObject.fromObject(data);
         Iterator iterator_3 = dataObj_3.keys();
         try {
         mOutputStream.write(("重复打印").getBytes("cp936"));
         sendCommand(0x0a);
         mOutputStream.write(("   ").getBytes("cp936"));
         sendCommand(0x0a);
         //内容
         sendCommand(0x1B, 0x61, 0x00);//左对齐
         while (iterator_3.hasNext()) {
         String key = iterator_3.next().toString();
         String value = dataObj_3.optString(key);
         Log.i(TAG, "sendPrintCommand: =" + key + ":" + value);
         mOutputStream.write((key + ":" + value).getBytes("cp936"));
         sendCommand(0x0a);
         }
         mOutputStream.write(("  ").getBytes("cp936"));
         sendCommand(0x0a);
         sendCommand(0x1B, 0x64, 0x03);
         } catch (IOException e) {
         e.printStackTrace();
         }
         break;
         case 4: //凭证查询
         JSONObject dataObj_4 = JSONObject.fromObject(data);
         Iterator iterator_4 = dataObj_4.keys();
         try {
         mOutputStream.write(("   ").getBytes("cp936"));
         sendCommand(0x0a);
         //内容
         sendCommand(0x1B, 0x61, 0x00);//左对齐
         while (iterator_4.hasNext()) {
         String key = iterator_4.next().toString();
         String value = dataObj_4.optString(key);
         Log.i(TAG, "sendPrintCommand: =" + key + ":" + value);
         if (!"couponlog".equals(key)) {
         switch (key) {
         case "title":
         key = "票种";
         break;
         case "buynum":
         key = "总购次数";
         break;
         case "surplusnm":
         key = "可用次数";
         break;
         case "moblie":
         key = "客户手机号";
         break;
         case "buytime":
         key = "购票时间";
         break;
         case "allowdate":
         key = "有效期";
         break;
         }
         mOutputStream.write((key + ":" + value).getBytes("cp936"));
         sendCommand(0x0a);
         } else {
         JSONArray couponArray = dataObj_4.optJSONArray("couponlog");
         Log.i(TAG, "sendPrintCommand: couponArray=" + couponArray.size());
         if (couponArray != null && couponArray.size() > 0) {
         for (int i = 0; i < couponArray.size(); i++) {
         JSONObject dataObj = JSONObject.fromObject(couponArray.get(i));
         Iterator iterator = dataObj.keys();
         try {
         //内容
         mOutputStream.write(("--------------------").getBytes("cp936"));
         sendCommand(0x0a);
         while (iterator.hasNext()) {
         String k = iterator.next().toString();
         String v = dataObj.optString(k);
         switch (k) {
         case "id":
         k = "序号";
         break;
         case "createtime":
         k = "消费时间";
         break;
         case "couponnum":
         k = "消费数量";
         break;
         }
         mOutputStream.write((k + ":" + v).getBytes("cp936"));
         sendCommand(0x0a);
         }
         } catch (Exception e) {
         e.printStackTrace();
         }
         }
         }
         }

         }
         mOutputStream.write(("  ").getBytes("cp936"));
         sendCommand(0x0a);
         sendCommand(0x1B, 0x64, 0x03);
         } catch (IOException e) {
         e.printStackTrace();
         }

         break;
         }
         **/
    }

    private void sleep(int ms) {
        try {
            java.lang.Thread.sleep(ms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DeviceService.logout();
        stop = true;
        //PrinterPowerOff();
        Log.e(TAG, "onDestroy");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PrintEvent event) {
    }
}
