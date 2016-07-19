package com.weibo.runfail;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
/*
 * <pre>
 *      根据ant/mvn 执行日志，执行失败用例
 * </pre>
 * @author hugang
 */
public class RunFailedCases {
	
	// 失败日志名
//	public static final String LOGNAME = "725.txt";
//	// 日志类型，支持MVN, ANT等2种
//	public static final String LOGTYPE = "MVN";
//	
//	// 失败日志名
	public static final String LOGNAME = "TEST-com.weibo.cases.suite.HugangTestSuite.txt";
	// 日志类型，支持MVN, ANT等2种
	public static final String LOGTYPE = "ANT";
	// 运行结果日志名，无需修改
	public static final String RESULTLOG = "Result.txt";
	// 执行用例方式：
	// 1.类级别并发（方法串行）
	// 2.方法级别并发
	// 3.串行
	// 默认以1.类级别并发（方法串行）执行
	public static final int RUNTYPE = 1;
	
	public static void main(String[] args) throws ExecutionException, IOException {		
		// 记录失败用例，key:Class , value:List of methods
		Map<Class<?>, List<String>> failMap;
		
		// 失败用例日志路径
		String logPath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "src"
				+ System.getProperty("file.separator") + "test"
				+ System.getProperty("file.separator") + "java"
				+ System.getProperty("file.separator") + "com"
				+ System.getProperty("file.separator") + "weibo"
				+ System.getProperty("file.separator") + "runfail"
				+ System.getProperty("file.separator")
				+ LOGNAME;
		
		// 结果日志路径
		String resultPath = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "src"
				+ System.getProperty("file.separator") + "test"
				+ System.getProperty("file.separator") + "java"
				+ System.getProperty("file.separator") + "com"
				+ System.getProperty("file.separator") + "weibo"
				+ System.getProperty("file.separator") + "runfail"
				+ System.getProperty("file.separator")
				+ RESULTLOG;
		
		System.out.println(logPath);
		System.out.println(resultPath);

		// "\"的转义字符
		logPath = logPath.replace("\\", "\\\\");

		// 简单工厂模式和策略模式, 根据不同的LOGTYPE创建不同实例
		FailCasesContext fcc = new FailCasesContext(LOGTYPE);
		// 通过扫日志，拿到对应失败case的Map		
		failMap = fcc.getFailCases(logPath);
		
		// 执行失败用例
		ExecuteCases ec = new ExecuteCases();
		// 执行
		switch(RUNTYPE){
			case 1:
				ec.executorClassCases(failMap, logPath, resultPath, LOGTYPE);
				break;
			case 2:
				ec.executorMethodCases(failMap, logPath, resultPath, LOGTYPE);
				break;
			case 3:
				ec.SerialExecutorCases(failMap, logPath, resultPath, LOGTYPE);
				break;
			default:
				ec.executorClassCases(failMap, logPath, resultPath, LOGTYPE);
				break;
		}
			
	}

}
