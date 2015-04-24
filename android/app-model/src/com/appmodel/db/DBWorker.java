package com.appmodel.db;

import android.os.Looper;
import android.support.v4.util.LongSparseArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Handler;

import com.duowan.mobile.utils.YLog;

public class DBWorker extends Thread {
	private final DBReqQueue tasks = new DBReqQueue();
	
	private LongSparseArray<List<IDBObserver>> observers = null;
    DBEngineAdapter adapter = null;

    Handler notifier = new Handler(Looper.getMainLooper());

    // flag to tell thread exit
	private volatile int terminate = 1; // not 0 to exit thread

    public DBWorker(LongSparseArray<List<IDBObserver>> observers, DBEngineAdapter adapter) {
		this.observers = observers;
        this.adapter = adapter;
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

	public void Term() {
		// tell the thread to exit;
		terminate = 1; 
		DBReqBase req = new DBReqBase();
		req.appid = 0;
		this.PutDBReq(req);  // an empty req to wake the thread
		try {
			this.join(1000000L); // milliseconds
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!this.isInterrupted()) {
			this.interrupt();
		}
        adapter.TermDB();
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

	private void ExecuteTask() {
		while (true) {
			if (terminate != 0)
				break;
			DBReqBase task = null;
			try {
				task = tasks.poll(10000L);
			} catch (Exception e) {
				YLog.info("DB", "DBWorkerBase::ExecuteTask Exception: " + e.getMessage());
			}
			try{
				DBRspBase rsp = adapter.Execute(task);
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
			boolean ret = adapter.PrepareDB();
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
          adapter.TermDB();
		} // finally
		YLog.info("DB", "DBWorkder::run is exiting...");
	} // run
}; // end calss DBWorkerBase

