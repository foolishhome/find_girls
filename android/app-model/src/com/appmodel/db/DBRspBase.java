package com.appmodel.db;

// user extends this base class to provide more info
public class DBRspBase {

    public static final int EXCEPTION = -2;
    public static final int FAILED = -1;
    public static final int SUCCESS = 0;

	public long appid;
	public long cmd;
    /**
     * 默认SUCCESS是成功。EXCEPTION为未处理的异常。统一一致。DB操作还是蛮多的。
     * 但是具体的错误码，可以在派生类里面加。
     * 因为还可以根据cmd来判断。即使重复也无所谓。
     */
	public long resultCode; // operation result code
	public String info;
	public long reqId;
	// extends to implement others
/*
    public boolean isSucceed() {
        if (cmd == DBAppConst.DBImTaskType.INSERT)
            return resultCode > 0;
        return resultCode == 0;
    }*/
};
