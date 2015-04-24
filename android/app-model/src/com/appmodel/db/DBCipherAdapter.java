package com.appmodel.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.util.LongSparseArray;

import com.duowan.mobile.utils.YLog;

//import net.sqlcipher.database.SQLiteDatabase;

public class DBCipherAdapter implements DBEngineAdapter {
    protected Context appcontext = null;
    protected int appversion = 1;
    protected String dbname = null;

    LongSparseArray<IDBProcessor> processors = null;
    SQLiteDatabase _db = null;

    static public final String PWD = "da451FH_w8";
	static private DBCipherOpenHelper helper = null;

	public DBCipherAdapter(Context appcontext, int appversion, String dbname,
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
		YLog.debug("YY", "[+]DBCipherWorker::Execute");
		if (task == null)
			return null;

		YLog.debug("YY", "DBCipherWorker::Execute get task to exe, appid is: " + task.appid + " priority: " + String.valueOf(task.priority));
		DBRspBase rsp = null;
		try {
            // remark it if you include sqlcipher pom
//			db = helper.getWritableDatabase(PWD);
            _db = helper.getWritableDatabase();

			IDBProcessor prs = null;
			prs = processors.get(task.appid);
			if (prs != null) {
				YLog.debug("DB", "DBCipherWorker get processor to exe...");
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
			YLog.debug("DB", "DBCipherWorker::Execute const context parameters");
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
		YLog.debug("YY", "[+]DBCipherWorker::InitDB");
		if (db == null)
			return;

		for (int i = 0; i < processors.size(); i++) {
			IDBProcessor processor = processors.valueAt(i);
			String sql = processor.initDBSQL();
			if (sql != null && sql.length() > 5) {
				db.execSQL(sql);
			}
		}
	}

	public void MigrateDB(SQLiteDatabase db, int oldversion, int appversion) {
		YLog.debug("YY", "[+]DBCipherWorker::MigrateDB");
		if (db == null)
			return;

		for (int i = 0; i < processors.size(); i++) {
			long appid = processors.keyAt(i);
			IDBProcessor processor = processors.valueAt(i);
			String sql = processor.migrateDBSQL(appid, oldversion,
					appversion);
			if (sql != null && sql.length() > 5) {
				db.execSQL(sql);
			}
		}
	}

	@Override
	public boolean PrepareDB() {
		YLog.debug("YY", "[+]DBCipherWorker::PrepareDB");
		boolean ret = true;
		try {
            // remark it if you include sqlcipher pom
//			SQLiteDatabase.loadLibs(appcontext);
			helper = new DBCipherOpenHelper(appcontext, appversion, dbname, this);
		} catch (Exception e) {
			YLog.debug("DB", e.getMessage());
			ret = false;
		}
		return ret;
	}

}; // end class DBCipherWorker


