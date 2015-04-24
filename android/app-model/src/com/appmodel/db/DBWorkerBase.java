package com.appmodel.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.LongSparseArray;
import com.duowan.mobile.utils.YLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DBWorkerBase extends Thread {
	
	private final DBReqQueue tasks = new DBReqQueue();
	
	protected Context appcontext = null;
	protected int appversion = 1;
	protected String dbname = null;

	protected LongSparseArray<IDBProcessor> processors = null;
	private LongSparseArray<List<IDBObserver>> observers = null;

	// flag to tell thread exit
	private volatile int terminate = 1; // not 0 to exit thread

    Handler notifier = new Handler(Looper.getMainLooper());

	DBWorkerBase(Context appcontext, int appversion, String dbname,
			LongSparseArray<IDBProcessor> processors,
			LongSparseArray<List<IDBObserver>> observers) {
		this.appcontext = appcontext;
		this.dbname = dbname;
		this.appversion = appversion;
		this.processors = processors;
		this.observers = observers;
		String info = "DBWorkerBase::ctor constructor file version " + this.appversion
				+ " passed version: " + appversion;
		YLog.info("DB", info);
	}

	public boolean Init() {
		if (terminate > 0) {
			terminate = 0;
			// can do this without lock, because the outer service
			// lock it and the thread have not been started
			this.start(); // start the thread
		}
		return true;
	}

	public void TermDB() {
		
	}
	
	public void Term() {
		// tell the thread to exit;
		terminate = 1; 
		DBReqBase req = new DBReqBase();
		req.appid = 0;
		this.PutDBReq(req);  // an empty req to wake the thread
		try {
			this.join(1000000L); // milliseconds
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!this.isInterrupted()) {
			this.interrupt();
		}
        this.TermDB();
	}

	public int PutDBReq(DBReqBase req) {
		YLog.debug("DB", "[+]DBWorkerBase::putDBReq");
		if (req != null) {
			tasks.push(req);
			return 0;
		}
		return 1;
	}

	private void NotifyObservers(DBRspBase rsp) {
		if (rsp == null)
			return;

        final DBRspBase interRsp = rsp;
        notifier.post(new Runnable()
        {
            @Override
            public void run()
            {
                List<IDBObserver> obs = null;
                synchronized (observers) {
                    obs = observers.get(interRsp.appid);
                    if (obs == null)
                        return;

                    ArrayList<IDBObserver> observers = new ArrayList<IDBObserver>(obs);
                    Iterator<IDBObserver> it = observers.iterator();
                    //Iterator<IDBObserver> it = obs.iterator();
                    while (it.hasNext()) {
                        final IDBObserver observer = it.next();
                        YLog.debug("DB", "DBWorkerBase::NotifyObservers, notify one observer...");
                        observer.Notify(interRsp);
                /*notifier.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        observer.Notify(interRsp);
                    }
                });*/
                    }
                } // synchronized
            }
        });
	}

	protected DBRspBase Execute(DBReqBase task) {
		return null;
	}

	public void InitDB(SQLiteDatabase db) {

	}

	public void MigrateDB(SQLiteDatabase db, int oldversion, int appversion) {
		
	}

	protected boolean PrepareDB() {
        return true;
	}

	private void ExecuteTask() {
		while (true) {
			// check if to exit
			if (terminate != 0)
				break;
			// YLog.info("DB", "DBWorkerBase::ExecuteTask...");
			// TODO Execute Batch task
			DBReqBase task = null;
			try {
				task = tasks.poll(10000L);
			} catch (Exception e) {
				YLog.info("DB", "DBWorkerBase::ExecuteTask Exception: " + e.getMessage());
			}
			try{
				DBRspBase rsp = this.Execute(task);
                if (rsp != null && DBRspBase.EXCEPTION != rsp.resultCode)
                {
                    this.NotifyObservers(rsp);
                }
			}catch(Exception e){
				e.printStackTrace();
			}
			
		} // end while
	}

	@Override
	public void run() {
		YLog.debug("DB", "DBWorkerBase::run is running...");
		try {
			boolean ret = this.PrepareDB();
			if (ret) {
				YLog.info("DB", "DBWorkerBase::run Init DB OK, Now start executing task......");
				this.ExecuteTask();
			} else {
				YLog.error("DB", "DBWorkerBase::run Fail to init DB...");
			}
			YLog.debug("DB", "DBWorkerBase::run is told to exit...");
		} catch (Exception e) {
			e.printStackTrace();
			YLog.error("DB", "DBWorkerBase::run Exception: " + e.getMessage());
		} finally {
          this.TermDB();
		} // finally
		YLog.info("DB", "DBWorkder::run is exiting...");
	} // run

}; // end calss DBWorkerBase

