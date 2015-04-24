package com.appmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

public abstract class Model {

    private Application application;
    private Handler ioHandler;

    public abstract void unInit();

    public abstract void clear();

    public void init(Application application, Handler ioHandler) {
        this.application = application;
        this.ioHandler = ioHandler;
    }

    protected Application getApplication() {
        return application;
    }

    protected String getString(int resId, Object... formatArgs) {
        return application.getString(resId, formatArgs);
    }

    protected SharedPreferences getCommonPreference() {
        return application.getSharedPreferences("CommonPref", Context.MODE_PRIVATE);
    }

    protected void runInIoHandler(Runnable runnable) {
        ioHandler.post(runnable);
    }

    public Handler getIoHandler() {
        return ioHandler;
    }
}
