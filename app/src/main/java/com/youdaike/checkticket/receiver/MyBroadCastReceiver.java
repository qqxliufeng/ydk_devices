package com.youdaike.checkticket.receiver;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.youdaike.checkticket.activity.LoginActivity;

public class MyBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_USER_PRESENT.equals(intent.getAction()) || Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Activity.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(Context.KEYGUARD_SERVICE);
            lock.disableKeyguard();//关闭系统锁屏
            Intent startIntent = new Intent(context,LoginActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(startIntent);
        }
    }
}
