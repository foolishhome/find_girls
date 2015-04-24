package com.appmodel.db;

public interface IDBObserver {
	/**
	 * CallBack to notify DB operation
	 * 
	 * @param rsp
	 *            DB operation response return from IDBProcessor
	 */
	public abstract void Notify(DBRspBase rsp);

}