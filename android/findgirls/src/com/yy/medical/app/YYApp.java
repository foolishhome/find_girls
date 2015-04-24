package com.yy.medical.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.appmodel.YYSdkConfig;
import com.duowan.mobile.utils.YLog;
import com.yy.appmodel.YYAppModel;

public class YYApp extends Application
{

    @TargetApi(11)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("yy_medical", "startApp YYApp onCreate");

//        File cacheDir = StorageUtils.getIndividualCacheDirectory(this);
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).threadPoolSize(2) // default
//                .threadPriority(Thread.NORM_PRIORITY - 2) // default
//                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
//                .memoryCacheSizePercentage(13) // default
//                .discCache(new LimitedAgeDiscCache(cacheDir, 60 * 60 * 24 * 10)) // default
//                .imageDownloader(new BaseImageDownloader(this)) // default
//                .imageDecoder(new BaseImageDecoder(true)) // default
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//                .discCache(new TotalSizeLimitedDiscCache(StorageUtils.getIndividualCacheDirectory(getApplicationContext()), new Md5FileNameGenerator(), 50 * 1024 * 1024))
//                .writeDebugLogs().build();
//
//        ImageLoader.INSTANCE.init(config);
//        Image.initMessageOptions(this);

        if (Build.VERSION.SDK_INT >= 11 && YYSdkConfig.isDebugMode(this)) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog().build());
        }

        if (!YYAppModel.INSTANCE.hasInit()) {
            YYAppModel.INSTANCE.init(this);
        }

//        LiveService.start(this);

//just for test notifycation message
//        Intent intent = new Intent(this, ChannelActivity.class);
//       setMessageNotification(NotificationUtil.ID_NOTIFY_LIVE_START, null, "节目预告", "【网医】预防中暑的小窍门22 开播了", "预防中暑的小窍门22 开播了",36035442,36035442, intent);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        YLog.warn(this, "on low memory");
//        ImageLoader.INSTANCE.clearMemoryCache();
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                return networkInfo.isAvailable();
            }
        }
        return false;
    }
}
