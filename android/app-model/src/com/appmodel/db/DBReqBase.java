package com.appmodel.db;

public class DBReqBase {
	public long appid;
	public long priority;
	public long cmd;
	public long reqId; //用来标示请求 


	public DBReqBase() {
		priority = DBAppConst.DB_REQ_PRIORITY_NORMAL;
	}

	public DBReqBase(long appid, long cmd, long reqId) {
		this.appid = appid;
		this.cmd = cmd;
		this.reqId = reqId;
		this.priority = DBAppConst.DB_REQ_PRIORITY_NORMAL;
	}
};
