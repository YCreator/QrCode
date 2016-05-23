package com.genye.myapplication.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolManager  {
	
	private static ThreadPoolManager threadPoolManager;
	private static ThreadPoolExecutor pool;

	public ThreadPoolManager() {
	}
	
	public static ThreadPoolManager getInstance() {
		if (threadPoolManager == null) {
			threadPoolManager = new ThreadPoolManager();
		}
		return threadPoolManager;
	}
	
	public void initPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 
				keepAliveTime, unit, workQueue);
	}
	
	public void initPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 
				keepAliveTime, unit, workQueue, handler);
	}
	
	public void initPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 
				keepAliveTime, unit, workQueue, threadFactory);
	}
	
	public void initPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 
				keepAliveTime, unit, workQueue, threadFactory, handler);
	}
	
	public void startTask(Runnable runnable) {
		if (pool == null) {
			pool = new ThreadPoolExecutor(3,5,200,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(10));
		}
		pool.execute(runnable);
	}
}
