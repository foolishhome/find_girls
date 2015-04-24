package com.appmodel.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.duowan.mobile.utils.YLog;

@TargetApi(14)
public class Api14AppLifecycle extends AppLifecycle implements Application.ActivityLifecycleCallbacks {

    private int activityCount;

    public Api14AppLifecycle(Application application) {
        application.registerActivityLifecycleCallbacks(this);
    }

    public Api14AppLifecycle(Application application, int initialCount) {
        this(application);
        activityCount = initialCount;
    }

    @Override
    public boolean isAtBackground() {
        return activityCount <= 0;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        //do nothing
    }

    @Override
    public void onActivityStarted(Activity activity) {
        //do nothing
    }

    @Override
    public void onActivityResumed(Activity activity) {
        YLog.verbose(this, "onActivityResumed");
        activityCount++;
    }

    @Override
    public void onActivityPaused(Activity activity) {
        YLog.verbose(this, "onActivityPaused");
        activityCount--;
    }

    @Override
    public void onActivityStopped(Activity activity) {
        //do nothing
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        //do nothing
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        //do nothing
    }
}
