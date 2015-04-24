package com.appmodel.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class DBReqQueue {
	
	private int total  = 0;
	private final int MAX_AVAILABLE = 100;
	private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);
    private final List<DBReqBase> low_tasks    = new ArrayList<DBReqBase>();
    private final List<DBReqBase> normal_tasks = new ArrayList<DBReqBase>();
	private final List<DBReqBase> high_tasks   = new ArrayList<DBReqBase>();
	
	// TODO How to do if too many reqs?
	protected synchronized void enqueue(DBReqBase req){
	  if (req.priority == DBAppConst.DB_REQ_PRIORITY_HIGH){
		  high_tasks.add(req);
	  } else if (req.priority == DBAppConst.DB_REQ_PRIORITY_LOW){
		  low_tasks.add(req);
	  } else {
		  normal_tasks.add(req);
	  }
	  ++ total;
	}
	
	public void push(DBReqBase req){
	  this.enqueue(req);
	  available.release();
	}
	
	public synchronized DBReqBase dequeue(){
		if (total < 1)
			return null;
		
		if (!high_tasks.isEmpty()){
			--total;
			return high_tasks.remove(0);
		}
		
		if (!normal_tasks.isEmpty()){
			--total;
			return normal_tasks.remove(0);
		}
		
		if (!low_tasks.isEmpty()){
			--total;
			return low_tasks.remove(0);
		}
		return null;
	}
	
	// MILLISECONDS
	public DBReqBase poll(long timeout){
	  try {
		available.tryAcquire(timeout, TimeUnit.MILLISECONDS);
	  } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	  }
	  return dequeue();
	}

	public int size(){
		return total;
	}
	
};
