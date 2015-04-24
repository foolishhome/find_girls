package com.appmodel.util;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.List;

public class DefaultAppLifecycle extends AppLifecycle {

    private Application application;

    public DefaultAppLifecycle(Application application) {
        this.application = application;
    }

    public boolean isAtBackground() {
        ActivityManager activityManager = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();

        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfoList) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcessInfo.pid == myPid) {
                return false;
            }
        }
        return true;
    }
}
