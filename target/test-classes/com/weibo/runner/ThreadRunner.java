package com.weibo.runner;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * Runs all tests in parallel and waits for them to complete. 
 * Up to {@link #maxThreads} will be run at once.
 * 自定义runner: 单个测试类，方法并发执行
 * @hugang
 * 
 */
public class ThreadRunner extends BlockJUnit4ClassRunner {

	private AtomicInteger numThreads;
	
	public static int maxThreads = 25;
	
	public ThreadRunner(Class<?> klass) throws InitializationError {
		super(klass);
		// TODO Auto-generated constructor stub
		numThreads = new AtomicInteger(0);
	}
    
	protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
		while(numThreads.get() > maxThreads){
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){
				e.printStackTrace();
				return;
			}
		}
		
		numThreads.incrementAndGet();
		new Thread(new Test(method, notifier)).start();
	}
	
	protected Statement childrenInvoker(final RunNotifier notifier){
		return new Statement(){

			@Override
			public void evaluate() throws Throwable {
				// TODO Auto-generated method stub
				ThreadRunner.super.childrenInvoker(notifier).evaluate();
				while(numThreads.get() > 0){
					Thread.sleep(1000);
				}
			}
			
		};
	}
	
	
	
	
	
	
	
	class Test implements Runnable{
		private final FrameworkMethod method;
		private final RunNotifier notifier;
		
		public Test(FrameworkMethod method, RunNotifier notifier){
			this.method = method;
			this.notifier = notifier;
		}
		
		
		public void run() {
			ThreadRunner.super.runChild(method, notifier);
			// TODO Auto-generated method stub
			numThreads.decrementAndGet();
			
		}
		
	}
  
}
