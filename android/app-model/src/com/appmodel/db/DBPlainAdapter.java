package com.appmodel.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.LongSparseArray;

import com.duowan.mobile.utils.YLog;

public class DBPlainAdapter implements DBEngineAdapter {
    protected Context appcontext = null;
    protected int appversion = 1;
    protected String dbname = null;

    LongSparseArray<IDBProcessor> processors = null;

    static private DBPlainOpenHelper helper = null;
    private SQLiteDatabase _db = null;

	public DBPlainAdapter(Context appcontext, int appversion, String dbname,
                    LongSparseArray<IDBProcessor> processors) {
        this.appcontext = appcontext;
        this.dbname = dbname;
        this.appversion = appversion;
        this.processors = processors;
        String info = "DBWorkerBase::ctor constructor file version " + this.appversion
                + " passed version: " + appversion;
        YLog.info("DB", info);
    }

    @Override
	public void TermDB() {
        if (_db != null){
            _db.close();
            _db = null;
        }
		if (helper != null) { // must go with lock?
			helper.close();
			helper = null;
		}
	}

    @Override
	public DBRspBase Execute(DBReqBase task) {
		YLog.debug("YY", "[+]DBWorker::Execute");
		if (task == null)
			return null;

		YLog.debug("YY", "DBWorker::Execute get task to exe, appid is: " + task.appid + " priority: " + String.valueOf(task.priority));
		DBRspBase rsp = null;

		try {
            _db = helper.getWritableDatabase();

			IDBProcessor prs = null;
			prs = processors.get(task.appid);
			// TODO Can I do this, get the processor out and do without lock
			if (_db != null && prs != null) {
				YLog.debug("DB", "DBWorker get processor to exe...");
				rsp = prs.Process(task, _db);
			}
		} catch (Exception e) { // To further support different DB, use a based Exception class
			YLog.info("DB", "DBWoker::Execute Exception: " + e.getMessage());
            YLog.error("DB", "DBWoker::Execute Exception: " + e.getMessage());
			rsp = new DBRspBase();
			rsp.resultCode = DBRspBase.EXCEPTION;
			rsp.info = e.getMessage();
		}
		if (rsp != null) {
			YLog.debug("DB", "DBWorker::Execute const context parameters");
			rsp.appid = task.appid;
			rsp.cmd = task.cmd;
            rsp.reqId = task.reqId;
		}
        // 每次使用完后都关闭，保证崩溃后不损坏db，
        // 在开发调试过程中遇到多次损坏db的情况，不过这种方式效率很低，需要测试确认release是否需要这样
        if (_db != null){
            _db.close();
            _db = null;
        }
		return rsp;
	}

	public void InitDB(SQLiteDatabase db) {
		YLog.debug("YY", "[+]DBWorker::InitDB");
		if (db == null)
			return;
		
		for (int i = 0; i < processors.size(); i++) {
			String sql = processors.valueAt(i).initDBSQL();
			if (sql != null && sql.length() > 5) {
				db.execSQL(sql);
			}
		}
    }

	public void MigrateDB(SQLiteDatabase db, int oldversion, int appversion) {
		YLog.debug("YY", "[+]DBWorker::MigrateDB");
		if (db == null)
			return;

		for (int j = 0; j < processors.size(); j++) {
			long appid = processors.keyAt(j);
			IDBProcessor processor = processors.valueAt(j);
			String sql = processor.migrateDBSQL(appid, oldversion,
					appversion);
			YLog.verbose("TimTrack","before sql:" + sql );
			if( sql!=null ){
			    String [] sqls = null;  
			    sqls = sql.split(";");
			    
			    for (int i = 0; sqls!=null && i < sqls.length; i++) {
			    	String tempSql = sqls[i];
					if (tempSql != null && tempSql.length() > 5) {
						db.execSQL(tempSql);
					}
			    }
			}
			YLog.verbose("TimTrack","after sql:" + sql );
		}
	}

    @Override
	public boolean PrepareDB() {
		YLog.debug("YY", "[+]DBWorker::PrepareDB");
		boolean ret = true;
		try {
			helper = new DBPlainOpenHelper(appcontext, appversion, dbname,
					this);
		} catch (Exception e) {
			YLog.debug("DB", e.getMessage());
			ret = false;
		}
		return ret;
	}

}; // end class DBWorker

