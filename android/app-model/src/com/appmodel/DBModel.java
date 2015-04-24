package com.appmodel;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.v4.util.LongSparseArray;

import com.appmodel.util.notification.NotificationCenter;
import com.duowan.mobile.utils.YLog;
import com.appmodel.db.DBCipherAdapter;
import com.appmodel.db.DBEngineAdapter;
import com.appmodel.db.DBPlainAdapter;
import com.appmodel.db.DBReqBase;
import com.appmodel.db.DBWorker;
import com.appmodel.db.IDBObserver;
import com.appmodel.db.IDBProcessor;

import java.util.ArrayList;
import java.util.List;

public class DBModel extends Model {
	DBWorker worker = null;

	private boolean mIsAlive = false;
	private int count = 0;

	private Context appcontext = null;

    private Class class_DBAdapter = DBPlainAdapter.class;

	private int appversion = 0;
	private String dbname = null;

	protected final LongSparseArray<IDBProcessor> processors = new LongSparseArray<IDBProcessor>();
	protected final LongSparseArray<List<IDBObserver>> observers = new LongSparseArray<List<IDBObserver>>();

    public void clear()
    {
    }
    public boolean isAlive() {
        return mIsAlive;
    }
    @Override
    public void unInit() {
        NotificationCenter.INSTANCE.removeObserver(this);

        mIsAlive = false;
        count = 0;
        if (worker != null) {
            // stop
            worker.Term();
            // reset
            worker = null;
            YLog.info("DB", "Release DB Worker");
        }

        processors.clear();
        //下面必须设置为null，如果不空，未登录时会重新打开上一次登录时的信息
        appcontext = null;
        dbname = null;

    }

    @Override
    public void init(Application application, Handler ioHandler) {
        super.init(application, ioHandler);

        NotificationCenter.INSTANCE.addObserver(this);
    }

	/**
	 * Setup DB env
	 * 
	 * @param appversion
	 * @param dbpath   database full path
	 * @param dbtype   database type( sqlite, sqlcipher ...) only support
	 *                 sqlite at the moments
	 */
	public synchronized void setupService(Context appcontext, int appversion,
			String dbname, Class dbtype) {
		this.appcontext = appcontext;
		this.dbname = dbname;
		this.appversion = appversion;
		this.class_DBAdapter = dbtype;
		String info = "setupService file version " + this.appversion
				+ " passed version: " + appversion;
		YLog.info("DB", "DBCenter::setupService " + info);
	}

	public synchronized boolean startService() {
		if (worker == null &&
			// 这里会出现一个情况，当界面切到后台被销毁后，重新激活构造，会在还未setupService情况下调用startService，导致初始化数据库失败
			appcontext != null && dbname != null && appversion != 0) {
			String info = "DBCenter::startService passed version: " + appversion;
			YLog.info("YY", info);
            DBEngineAdapter adapter = null;
			if (class_DBAdapter == DBCipherAdapter.class){
                adapter = new DBCipherAdapter(appcontext, appversion,
                        dbname, processors);
			} else {
                adapter = new DBPlainAdapter(appcontext, appversion,
                        dbname, processors);
			}
            worker = new DBWorker(observers, adapter);
			YLog.debug("YY", "Worker create...");
			mIsAlive = true;
			return worker.Init();
		}
		return false;
	}
	
	/**
	 * Register an observer to get notification when DB operation done
	 * @param appid
	 * @param observer  observer to get notification
	 * @return 0 for successful
	 */
	public int RegisterObserver(long appid, IDBObserver observer) {

        YLog.info(DBModel.class.getName(), DBModel.class.getName() + ".RegisterObserver ", "");
        synchronized (observers) // local scope lock
		{
			List<IDBObserver> obs = observers.get(appid);
			if (obs == null) {
				obs = new ArrayList<IDBObserver>();
				observers.put(appid, obs);
			}
			if (!obs.contains(observer)) {
				obs.add(observer);
			}
		}
		return 0;
	}

	/**
	 * UnRegister an observer to stop getting DB notification
	 * @param appid
	 * @param observer  observer to unregister
	 * @return 0 for successful
	 */
	public int unRegisterObserver(long appid, IDBObserver observer) {

        YLog.info(DBModel.class.getName(), DBModel.class.getName() + ".unRegisterObserver ", "");
		synchronized (observers) // local scope lock
		{
			List<IDBObserver> obs = observers.get(appid);
			if (obs != null) {
				obs.remove(observer);
			}
		}
		return 0;
	}

	/**
	 * Register a processor to process DB business logic
	 * Make sure all the processors had been registered
	 * before the worker thread started
	 * RegisterProcessor/unRegisterProcessor must be executed
	 * in single thread env
	 * @param appid
	 * @param processor
	 * @return 0 for successful
	 */
	public int RegisterProcessor(long appid, IDBProcessor processor) {
		
		if (worker == null){
	      processors.put(appid, processor); // just insert or replace
	      return 0;
		}
        YLog.error("DB", "DBCenter::RegisterProcessor Fail, the thread had been started");
        return 1;
	}

	/**
	 * UnRegister a processor
	 * Make sure all the processors had been registered
	 * before the worker thread started or exited
	 * RegisterProcessor/unRegisterProcessor must be executed
	 * in single thread env
	 * @param appid
	 * @param processor
	 * @return 0 for successful
	 */
	public int unRegisterProcessor(long appid) {
		
		if (worker == null){
			processors.remove(appid); // just remove if any
			return 0;
		}
        YLog.error("DB", "DBCenter::unRegisterProcessor fail, the thread had been started");
		return 1;
	}

	/**
	 * Puts an DB request to DBCenter
	 * @param req req information
	 * @return 0 for successful
	 */
	public synchronized int putDBReq(DBReqBase req) {
		YLog.debug("DB", "[+]DBCenter::putDBReq");
		if (worker == null) {
			YLog.debug("DB", "DBCenter::putDBReq worker is null Put req fail, try to restart worker");
			startService();
		}
		if (worker == null)
			return 0;
		return worker.PutDBReq(req);
	}
}; // end class DBCenter
