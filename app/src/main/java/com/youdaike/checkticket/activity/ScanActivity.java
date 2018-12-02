package com.youdaike.checkticket.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

//import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.youdaike.checkticket.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ScanActivity extends BaseActivity
//        implements QRCodeReaderView.OnQRCodeReadListener
{
//    private static final String TAG = "ScanActivity";
//
//    private String mScanContent;
//    private SoundPool mSoundPool;
//
//    @BindView(R.id.activity_scan_qrcode)
//    QRCodeReaderView mQR_QRCode;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_scan);
//        ButterKnife.bind(this);
//        initQRCodeView();
//        initTone();
//        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Log.i("info", "landscape");
//        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Log.i("info", "portrait");
//        }
//    }
//
//    private void initQRCodeView() {
//        mQR_QRCode.setOnQRCodeReadListener(this);
//        // Use this function to enable/disable decoding
//        mQR_QRCode.setQRDecodingEnabled(true);
//        // Use this function to change the autofocus interval (default is 5 secs)
//        mQR_QRCode.setAutofocusInterval(2000L);
//        // Use this function to enable/disable Torch
//        mQR_QRCode.setTorchEnabled(true);
//        // Use this function to set front camera preview
//        mQR_QRCode.setFrontCamera();
//        // Use this function to set back camera preview
//        mQR_QRCode.setBackCamera();
//    }
//
//    @Override
//    public void onQRCodeRead(String text, PointF[] points) {
//        if (TextUtils.isEmpty(text)) {
//            return;
//        }
//        //屏蔽多次扫码
//        if (!text.equals(mScanContent)) {
//            mScanContent = text;
//            mSoundPool.play(1, 1, 1, 0, 0, 1);
//            Intent intent = new Intent();
//            intent.putExtra("ScanNumber", mScanContent);
//            setResult(RESULT_OK, intent);
//            finish();
//        }
//    }
//
//
//    /**
//     * 扫码成功提示音
//     */
//    private void initTone() {
//        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 5);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
//        mSoundPool.load(this, R.raw.tone, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        mQR_QRCode.startCamera();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        mQR_QRCode.stopCamera();
//    }
}
