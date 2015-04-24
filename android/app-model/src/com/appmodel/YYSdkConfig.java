package com.appmodel;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;


import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class YYSdkConfig {
    // 医疗的from可以用"yym17ip" "yym17and"了
    // 试听"yym12and";
    // 临时 yym05ip; yym05and
    public static final String ANDROID_FROM = "yym17and";
    public static final String APP_ID = "onlinemedical";
    public static final String APP_KEY = "797eea0e0da5f5a9bcb88f2a79918a76";
    private final String productName;
    private String name;


    public YYSdkConfig(String name, String productName) {
        this.name = name;
        this.productName = productName;
    }

    private void initLogging() {
    }

    public final static class LogPathConfig {
        private final String mLogPath;
        private final String mLogFileName;
        private final String mUncaughtExceptionsLogName;

        public LogPathConfig() {
            mLogPath = "ylog";
            mLogFileName = "logs.txt";
            mUncaughtExceptionsLogName = "uncaught_exception.txt";
        }

        /**
         * Get file path of the uncaught exceptions' log file by the given app
         * root directory.
         *
         * @param rootDir
         * @return File path of the uncaught exceptions' log file.
         */
        public String getUELogPath(String rootDir) {
            return connect(connect(rootDir, mLogPath), mUncaughtExceptionsLogName);
        }

        private static String connect(String s1, String s2) {
            return s1.endsWith(File.separator) ? s1 + s2 : s1 + File.separator + s2;
        }

        /**
         * Get uncaught exception log name.NOTE this is not a file path.
         * @return Uncaught exception log name
         */
        public String getUncaughtExceptionsLogName() {
            return mUncaughtExceptionsLogName;
        }

        /**
         * Get log relative directory, not including the app's root directory part.
         * @return Log relative directory.
         */
        public String getLogPath() {
            return mLogPath;
        }

        /**
         * Get log file name part, NOTE this is not a file path.
         * @return Log file name.
         */
        public String getLogFileName() {
            return mLogFileName;
        }
    }

    private final static LogPathConfig mLogPath = new LogPathConfig();
    public static LogPathConfig getLogPathConfig() {
        return mLogPath;
    }

    private static String getTagFromConfig() {
        return APP_ID;
    }

    private static AtomicReference<String> mClientVersion = new AtomicReference<String>();
    public static String getClientVersion() {
        return mClientVersion.get();
    }

    /**
     * Initialize model.
     */
    public void init(Application application, Handler handler) {
        initLogging();

    }

    public static boolean isDebugMode(Context context) {
        boolean debugAble = false;
        ApplicationInfo appInfo = null;
        PackageManager packMgmr = context.getPackageManager();
        try {
            appInfo = packMgmr.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {

        }
        if (appInfo != null) {
            debugAble = (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) > 0;
        }

        return debugAble;
    }

}