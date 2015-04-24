package com.appmodel.db;

public interface DBEngineAdapter {
    public void TermDB();
    public DBRspBase Execute(DBReqBase task);
    public boolean PrepareDB();
}
