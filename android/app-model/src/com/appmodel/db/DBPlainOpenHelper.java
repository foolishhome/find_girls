package com.appmodel.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.duowan.mobile.utils.YLog;

public class DBPlainOpenHelper extends SQLiteOpenHelper {
	DBPlainAdapter worker = null;

	public DBPlainOpenHelper(Context context, int appversion, String dbname,
			DBPlainAdapter worker) {
		super(context, dbname, null, appversion);
		this.worker = worker;
		String text = "On Constructe open helper, db: " + dbname + " version: "
				+ appversion;
		YLog.debug("DB", text);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		YLog.debug("DB", "Openhelper OnCreate");
		worker.InitDB(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String text = "OnUpgrade, oldversion: " + oldVersion + " new version: "
				+ newVersion;
		YLog.info("DB", text);
		if (worker != null) {
			YLog.debug("DB", "Openhelper onUpgrade do upgrade...");
			worker.MigrateDB(db, oldVersion, newVersion);
		}
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// super.onDowngrade(db, oldVersion, newVersion);
		YLog.debug("DB", "helper onDowngrade");
	}

};
