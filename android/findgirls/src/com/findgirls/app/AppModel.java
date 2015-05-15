package com.findgirls.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.appmodel.DBModel;
import com.appmodel.SDKModel;
import com.appmodel.YYSdkConfig;
import com.appmodel.db.DBAppConst;
import com.appmodel.db.DBPlainAdapter;
import com.appmodel.db.DBReqBase;
import com.appmodel.db.IDBObserver;
import com.appmodel.notification.SDKCallback;
import com.appmodel.util.Api14AppLifecycle;
import com.appmodel.util.AppLifecycle;
import com.appmodel.util.DefaultAppLifecycle;
import com.appmodel.util.StringUtils;
import com.appmodel.util.notification.NotificationCenter;
import com.duowan.mobile.utils.YLog;

public enum AppModel {

    INSTANCE {

    };

    private boolean hasInit = false;
    private boolean splashShowed = false;

    private DialogModel dialogModel;
    private SDKModel sdkModel;
    private DBModel dbModel;
    private String mDBName = "";
    private static final int dbtype = 0;

    //store
    private Handler mainThreadHandler;
    private Handler ioHandler;
    private Application application;
    private AppLifecycle appLifecycle;

    private AppModel() {
    }


    public void DBModelRegisterObserver(long appid, IDBObserver observer) {
        DBModel model = AppModel.INSTANCE.dbModel();
        if (model != null) {
            model.RegisterObserver(appid, observer);
        }
        else {
            YLog.debug(this, "null dbmodel operate DBModelRegisterObserver, appid=%d", appid);
        }
    }
    public void DBModelUnRegisterObserver(long appid, IDBObserver observer) {
        DBModel model = AppModel.INSTANCE.dbModel();
        if (model != null) {
            model.unRegisterObserver(appid, observer);
        }
        else {
            YLog.debug(this, "null dbmodel operate DBModelUnRegisterObserver, appid=%d", appid);
        }
    }
    public int DBModelPutDBReq(DBReqBase req) {
        DBModel model = AppModel.INSTANCE.dbModel();
        if (model != null) {
            return model.putDBReq(req);
        }
        else {
            YLog.debug(this, "null dbmodel operate DBModelPutDBReq");
        }
        return 0;
    }

    public DialogModel dialogModel() {
        return dialogModel;
    }
    public SDKModel sdkModel() {
        return sdkModel;
    }
    // 由于dbModel可能在退出账号后置空，所以外部不能随意操作
    private DBModel dbModel() {
        return dbModel;
    }
    public boolean dbIsAlive() {
        return dbModel != null && dbModel.isAlive();
    }

    /**
     * Initialize model.
     */
    public void init(final Application app) {
        this.application = app;

        mainThreadHandler = new Handler(Looper.getMainLooper());
        hasInit = true;
        YYSdkConfig config = new YYSdkConfig("findgirls", "findgirls_android"); // iphone productname = yymedical_iphone
        config.init(app, mainThreadHandler);
        HandlerThread ioThread = new HandlerThread("IoThread");
        ioThread.start();
        ioHandler = new Handler(ioThread.getLooper());
        initAppLifeCycle();

        reInit();
    }

    private void initAppLifeCycle() {
        if (Build.VERSION.SDK_INT >= 14) {
            appLifecycle = new Api14AppLifecycle(application, 0);
        } else {
            appLifecycle = new DefaultAppLifecycle(application);
        }
    }

    void reInit()
    {
        initCallbacks();
        initModels();
    }

    private void initCallbacks() {
        NotificationCenter.INSTANCE.addCallbacks(SDKCallback.class);
    }

    public boolean hasInit() {
        return hasInit;
    }

    private void initModels() {
        if (dialogModel == null)
            dialogModel = new DialogModel();
        dialogModel.init(application, ioHandler);

        if (sdkModel == null)
            sdkModel = new SDKModel();
        sdkModel.init(application, ioHandler);

        if (dbModel == null)
            dbModel = new DBModel();
        dbModel.init(application, ioHandler);
    }

    void unInitModels() {
        dialogModel.clear();
        sdkModel.clear();
        dbModel.clear();
    }

    public Handler mainThreadHandler() {
        return mainThreadHandler;
    }

    public Context context() {
        return application;
    }

    public boolean isAtBackground() {
        return appLifecycle.isAtBackground();
    }

    public boolean showSplash() {
        boolean old = splashShowed;
        splashShowed = true;
        return old;
    }

    void unInit() {
        unInitModels();
    }

    public void exit(Context context) {
        splashShowed = false;
        YLog.debug(this, "quitApp");

        closeDB();
        unInit();

//        LiveService.stop(application);
        NotificationCenter.INSTANCE.removeAll();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }, 500);
    }

    public SharedPreferences getCommonPreference() {
        return application.getSharedPreferences("CommonPref", Context.MODE_PRIVATE);
    }

    public void closeDB() {
        if (dbModel != null)
            dbModel.unInit();
        //dbModel = null;
        mDBName = "";
        YLog.info("DB", "Close DB");
    }

    public void setupDBProcessor(String dbname) {
        if (StringUtils.isNullOrEmpty(dbname) || mDBName.equals(dbname))
            return;

/*
        if (dbModel == null) {
            YLog.verbose("DB", "new DBModel...");
            dbModel = new DBModel();
            dbModel.init(application, ioHandler);
        }
*/

        dbModel.setupService(application.getApplicationContext(),
                DBAppConst.app_version, dbname, DBPlainAdapter.class);
        YLog.verbose("DB", "Setup DB Processor1...");
//        dbModel.RegisterProcessor(DBAppConst.IM_CHAT_APP_ID, new ImProcessor());

        dbModel.startService();
        YLog.verbose("DB", "Setup DB startService");
    }

    public boolean isNetWorkOk(){
            return false;
    }
    public void setNetWorkState(boolean bState){
    }
}
