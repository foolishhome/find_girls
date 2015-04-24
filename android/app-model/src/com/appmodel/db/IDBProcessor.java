package com.appmodel.db;


public interface IDBProcessor {

	/**
	 * When the database create, will call this function to init the DB
	 * 
	 * @return return the creation SQL of the app module
	 */
	public abstract String initDBSQL();

	/**
	 * At the registration step, the app version is higher than the DB saved app
	 * version notify user to migration the DB to the latest version. User must
	 * put a task to migrate the DB
	 * 
	 * @param appid
	 * @param currentversion
	 *            current DB saved app version
	 * @param regversion
	 *            the registration app version
	 * @return return the migration SQL of the app module
	 */
	public abstract String migrateDBSQL(long appid, int currentversion,
			int regversion);

	/**
	 * CallBack for user to Process DB operation response
	 * 
	 * @param req
	 *            req information
	 * @param db
	 *            db handle
	 * @return processed result
	 */
	public abstract DBRspBase Process(DBReqBase req, Object database);

}