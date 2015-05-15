package com.findgirls.app;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.appmodel.YYSdkConfig;
import com.duowan.mobile.utils.YLog;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class App extends Application
{

    @TargetApi(11)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.v("find_girl", "startApp App onCreate");

        initImageLoader();

        if (Build.VERSION.SDK_INT >= 11 && YYSdkConfig.isDebugMode(this)) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork()   // or .detectAll() for all detectable problems
                    .penaltyLog().build());
        }

        if (!AppModel.INSTANCE.hasInit()) {
            AppModel.INSTANCE.init(this);
        }

//        LiveService.start(this);

//just for test notifycation message
//        Intent intent = new Intent(this, ChannelActivity.class);
//       setMessageNotification(NotificationUtil.ID_NOTIFY_LIVE_START, null, "节目预告", "【网医】预防中暑的小窍门22 开播了", "预防中暑的小窍门22 开播了",36035442,36035442, intent);
    }

    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(false) // default
                .cacheOnDisk(false) // default
                .considerExifParams(false) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .build();

        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "UniversalImageLoader/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(10)
                .diskCache(new LimitedAgeDiscCache(cacheDir, 60 * 60 * 24 * 10))
                .diskCacheSize(50 * 1024 * 1024)
                .imageDownloader(new BaseImageDownloader(this))
                .imageDecoder(new BaseImageDecoder(true))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        YLog.warn(this, "on low memory");
        ImageLoader.getInstance().clearMemoryCache();
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

    public static String getAppVersion(Context context) {
        if (context == null) {
            return "";
        }
        PackageInfo packInfo = getPackageInfo(context);
        if (packInfo != null && packInfo.versionName == null) {
            return "";
        }
        if (packInfo.versionName.contains("SNAPSHOT")) {
            return packInfo.versionName.substring(0, packInfo.versionName.lastIndexOf('.')) + "." + getSvnBuildVersion(context);
        } else {
            return packInfo.versionName;
        }
    }
    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo packInfo = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            YLog.error("PackageManager.NameNotFoundException context", e);
        }
        return packInfo;
    }
    private static int getSvnBuildVersion(Context context) {
        int svnBuildVer = 0;
        try {
            if (context != null) {
                String pkgName = context.getPackageName();
                ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(pkgName, PackageManager.GET_META_DATA);
                svnBuildVer = appInfo.metaData.getInt("SvnBuildVersion");
                YLog.info("yymedical","SvnBuildVersion:%d",svnBuildVer);
            }
        } catch (Exception e) {
            YLog.error(App.class, e);
        }

        return svnBuildVer;
    }
}
