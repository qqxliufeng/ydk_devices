package com.youdaike.checkticket.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.youdaike.checkticket.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by yuanxx on 2016/12/19.
 */

public class VersionUtil {
    private static final String TAG = "VersionUtil";
    private Context mContext; //上下文
    private String apkUrl; //apk下载地址
    private String savePath; //apk保存到SD卡的路径
    private String saveFileName; //完整路径名
    private TextView mTitle; //下载标题
    private ProgressBar mProgress; //下载进度条控件
    private static final int DOWNLOADING = 1; //表示正在下载
    private static final int DOWNLOADED = 2; //下载完毕
    private static final int DOWNLOAD_FAILED = 3; //下载失败
    private int progress; //下载进度
    private boolean cancelFlag = false; //取消下载标志位
    private String updateDescription = "你的APP版本过低，请升级后使用"; //更新内容描述信息
    private AlertDialog mNoticeDialog, mDownloadDialog; //表示提示对话框、进度条对话框

    /**
     * 构造函数
     */
    public VersionUtil(Context context) {
        this.mContext = context;
    }

    /**
     * 显示更新对话框
     *
     * @param serverVersionCode 从服务器获取的版本号
     * @param url               APK路径
     * @param isForceUpdate     是否强制更新 0-否、1-是
     */
    public void showNoticeDialog(int serverVersionCode, String url, String isForceUpdate, String updatetips) {
        //如果版本最新，则不需要更新
        int versionCode = getVersionCode();
        Log.d(TAG, "showNoticeDialog(): versionCode = " + versionCode + ",serverVersionCode = " + serverVersionCode + ", url = " + url + ", isForceUpdate = " + isForceUpdate + ", updatetips = " + updatetips + "");
        if (versionCode < 0 || serverVersionCode <= versionCode) {
            return;
        }
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            savePath = Environment.getExternalStorageDirectory().getPath() + "/youdaike/";
        } else {
            savePath = mContext.getFilesDir().getPath() + "/youdaike/";
        }
        saveFileName = savePath + "youdaike_" + serverVersionCode + ".apk";
        apkUrl = url;
        if (!TextUtils.isEmpty(updatetips)) {
            updateDescription = updatetips;
        }

        mNoticeDialog = new AlertDialog.Builder(mContext).create();
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.common_dialog, null);
        mNoticeDialog.setView(view, 0, 0, 0, 0);
        TextView title = (TextView) view.findViewById(R.id.common_dialog_title);
        TextView text = (TextView) view.findViewById(R.id.common_dialog_text);
        TextView agree = (TextView) view.findViewById(R.id.common_dialog_ok);
        TextView cancel = (TextView) view.findViewById(R.id.common_dialog_cancel);
        title.setText("发现新版本");
        text.setText(updateDescription);
        agree.setText("立即更新");
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNoticeDialog.dismiss();
                showDownloadDialog();
            }
        });
        cancel.setText("稍后提醒");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNoticeDialog.dismiss();
            }
        });
        //是否强制更新
        if ("0".equals(isForceUpdate)) {
            cancel.setVisibility(View.VISIBLE);
        } else {
            cancel.setVisibility(View.GONE);
        }
        mNoticeDialog.setCancelable(false);
        mNoticeDialog.show();
    }

    /**
     * 显示进度条对话框
     */
    public void showDownloadDialog() {
        mDownloadDialog = new AlertDialog.Builder(mContext).create();
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.progressbar_update, null);
        mTitle = (TextView) view.findViewById(R.id.title);
        mProgress = (ProgressBar) view.findViewById(R.id.progressbar);
        mTitle.setText("正在下载");
        mDownloadDialog.setView(view, 0, 0, 0, 0);
        mDownloadDialog.setCancelable(false);
        mDownloadDialog.show();
        //下载apk
        downloadAPK();
    }

    /**
     * 下载apk的线程
     */
    public void downloadAPK() {
        Log.d(TAG, "downloadAPK():apkUrl=" + apkUrl + ",savePath=" + savePath + ",saveFileName=" + saveFileName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apkUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    int length = conn.getContentLength();
                    InputStream is = conn.getInputStream();
                    File file = new File(savePath);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File ApkFile = new File(saveFileName);
                    FileOutputStream fos = new FileOutputStream(ApkFile);
                    int count = 0;
                    byte buf[] = new byte[1024];
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        //更新进度
                        mHandler.sendEmptyMessage(DOWNLOADING);
                        if (numread <= 0) {
                            //下载完成通知安装
                            mHandler.sendEmptyMessage(DOWNLOADED);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (!cancelFlag); //点击取消就停止下载.
                    fos.close();
                    is.close();
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 更新UI的handler
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOADING:
                    mTitle.setText("正在下载(" + progress + "%)");
                    mProgress.setProgress(progress);
                    break;
                case DOWNLOADED:
                    if (mDownloadDialog != null)
                        mDownloadDialog.dismiss();
                    installAPK();
                    break;
                case DOWNLOAD_FAILED:
                    Toast.makeText(mContext, "网络断开，请稍候再试", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 下载完成后自动安装apk
     */
    public void installAPK() {
        File apkFile = new File(saveFileName);
        if (!apkFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    //获取当前版本号
    private int getVersionCode() {
        // 包管理器
        PackageManager mPackageManager = mContext.getPackageManager();
        try {
            // 清单文件信息
            PackageInfo mPackageInfo = mPackageManager.getPackageInfo(
                    mContext.getPackageName(), 0);
            return mPackageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
