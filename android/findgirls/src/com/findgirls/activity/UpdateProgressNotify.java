package com.yy.medical.activity;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.NotificationCompat;

import java.util.concurrent.atomic.AtomicBoolean;


public class UpdateProgressNotify {
    private int mNotifyId = 0;
    private int mCurValue = 0;  //multi-thread access should be volatile
    private NotificationCompat.Builder mBuilder = null;
    private NotificationManager mManager = null;
    private HandlerThread mProgressThread = null;
    private Handler mProgressHandler = null;
    private Runnable mProgressRunnable = null;
    private AtomicBoolean mIsProgressed = new AtomicBoolean(true);

    private Context mContext;

    public UpdateProgressNotify(Context context, int notifyId) {
        mNotifyId = notifyId;
        mContext = context;
        init();
    }

    public void setRes(int smallIconId, int titleId, int textId) {
        if (-1 != smallIconId) {
            mBuilder.setSmallIcon(smallIconId);
        }

        if (-1 != titleId) {
            mBuilder.setContentTitle(mContext.getResources().getText(
                    titleId));
        }

        if (-1 != textId) {
            mBuilder.setContentText(mContext.getResources().getText(textId));
        }
        mManager.notify(mNotifyId, mBuilder.build());
    }

    public void setValue(int value, int maxValue) {
        if (maxValue <= 0) {
            return;
        }

        mCurValue = (int) Math.ceil(value * 100.0 / maxValue);
        if (mIsProgressed.get()) {
            mProgressHandler.postDelayed(mProgressRunnable, 300);
        }
        mIsProgressed.set(false);
    }

    public void setCurValue(int value) {
        mCurValue = value;
        if (mIsProgressed.get()) {
            mProgressHandler.postDelayed(mProgressRunnable, 300);
        }
        mIsProgressed.set(false);
    }

    public void cancle() {
        if (null != mProgressRunnable && null != mProgressHandler) {
            mProgressHandler.removeCallbacks(mProgressRunnable);
        }

        if (null != mProgressThread) {
            mProgressThread.quit();
            mProgressThread = null;
            mProgressHandler = null;
        }

        mManager.cancel(mNotifyId);
        mIsProgressed.set(true);
        mCurValue = 0;
    }

    public void start() {
        cancle();
        mProgressThread = new HandlerThread("ProgressThread");
        mProgressThread.start();
        mProgressHandler = new Handler(mProgressThread.getLooper());
        mIsProgressed.set(true);
    }

    private void init() {
        mManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);

        mProgressThread = new HandlerThread("ProgressThread");
        mProgressThread.start();
        mProgressHandler = new Handler(mProgressThread.getLooper());
        mProgressRunnable = new Runnable() {
            @Override
            public void run() {
                mIsProgressed.set(true);
                updateNotify();
            }
        };
    }

    private void updateNotify() {
        mBuilder.setProgress(100, mCurValue, false);
        mManager.notify(mNotifyId, mBuilder.build());
    }
}
