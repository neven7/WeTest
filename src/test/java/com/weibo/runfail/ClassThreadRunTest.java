package com.weibo.runfail;

/**
 * @hugang
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;



// 类并发执行，方法级别串行
public class ClassThreadRunTest implements Callable<List<Result>> {
	
	
	Class<?> testClass;
	List<String> failMethod;
	
	// 类的方法集 结果，线程安全的List
	List<Result> singClassResult = Collections.synchronizedList(new ArrayList<Result>());
	
	public ClassThreadRunTest(Class<?> testClass, List<String> failMethod){
		this.testClass = testClass;
		this.failMethod = failMethod;
	}
	
	// 线程执行体
	public List<Result> call() {
		// TODO Auto-generated method stub
		// 一个类下顺序执行方法
		for(int i = 0; i < failMethod.size(); i++){
			JUnitCore junitRunner = new JUnitCore();
			 Request request = Request.method(testClass, failMethod.get(i));
	         Result result = junitRunner.run(request);
	         singClassResult.add(result);     
		}
		return singClassResult;
	}

}
