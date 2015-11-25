package com.weibo.runfail;

import java.util.concurrent.Callable;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
/*
 * @author
 */
//Callable<Result>实现类，一个线程执行一个case， 返回结果Result
class ThreadRunTest implements Callable<Result>{
	private Class oneFailClass;
	private String oneFailMethod;
	
	public ThreadRunTest(Class oneFailClass, String oneFailMethod){
		this.oneFailClass = oneFailClass;
		this.oneFailMethod = oneFailMethod;
	}
	

	public Result call() throws Exception {
		// TODO Auto-generated method stub
		// JUnitCore执行JUnit用例
		 JUnitCore junitRunner = new JUnitCore();
		 Request request = Request.method(oneFailClass, oneFailMethod);
         Result result = junitRunner.run(request);
         
         return result;
	}
	
}
