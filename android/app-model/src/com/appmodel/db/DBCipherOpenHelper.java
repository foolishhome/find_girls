package com.appmodel.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.duowan.mobile.utils.YLog;

//import net.sqlcipher.database.SQLiteDatabase;
//import net.sqlcipher.database.SQLiteOpenHelper;

public class DBCipherOpenHelper extends SQLiteOpenHelper {
	DBCipherAdapter worker = null;

	public DBCipherOpenHelper(Context context, int appversion, String dbname,
			DBCipherAdapter worker) {
		super(context, dbname, null, appversion);
		this.worker = worker;
		String text = "DBCipherOpenHeler::ctor On Constructe open helper, db: " + dbname + " version: "
				+ appversion;
		YLog.debug("YY", text);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		YLog.debug("DB", "DBCipherOpenHeler::onCreate");
		worker.InitDB(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String text = "DBCipherOpenHeler::OnUpgrade, oldversion: " + oldVersion + " new version: " + newVersion;
		YLog.info("YY", text);
		if (worker != null) {
			YLog.debug("YY", "DBCipherOpenHeler::onUpgrade do upgrade...");
			worker.MigrateDB(db, oldVersion, newVersion);
		}
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// super.onDowngrade(db, oldVersion, newVersion);
		YLog.debug("DB", "DBCipherOpenHeler::onDowngrade");
	}

};
