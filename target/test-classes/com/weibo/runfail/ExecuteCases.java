package com.weibo.runfail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;


/*
 * @author hugang
 */
public class ExecuteCases {
	// 线程池大小
	final static int THREADCOUNT = 50;

	public void writeResult(String resultPath, List<Result> methodsResult,
			int failNum, int successNum, int casesNum, long runTime,
			String logPath, String logType) throws IOException {
		String filePath = resultPath;
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fop = new FileOutputStream(file);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss,SSS");
		fop.write("(一).Time's Result generated on: ".getBytes());
		fop.write(sdf.format(new Date()).getBytes());
		fop.write("\n".getBytes());

		StringBuffer sb = new StringBuffer();
		sb.append("(二).日志类型：" + logType);
		sb.append("\n");
		sb.append("(三).日志名：" + logPath);
		sb.append("\n");
		sb.append("===================== 结果集 =====================");
		sb.append("\n");
		sb.append("用例总数：" + casesNum);
		sb.append(", 成功数：" + successNum);
		sb.append(", 失败数：" + failNum);
		sb.append(", 运行时间：" + (runTime / 1000) / 60 + " 分钟 " + (runTime / 1000)
				% 60 + " 秒");
		sb.append("\n");
		sb.append("=================================================");
		sb.append("\n");
		sb.append("\n");
		fop.write(sb.toString().getBytes());
		for (int j = 0; j < methodsResult.size(); j++) {
			// 将JUnitCore失败信息中的换行符去掉, harmcrest中断言是多行显示
			String LineFailStr = methodsResult.get(j).getFailures().toString().replaceAll("\n", "");
			fop.write(LineFailStr.getBytes());
			fop.write("\n".getBytes());
			fop.write("\n".getBytes());
		}
		// 输出结果mvn格式, 便于前端展示
		// Tests run: 5, Failures: 2, Errors: 0, Skipped: 0
		System.out.println("Tests run: " + casesNum + ", Failures: " + failNum + ", Errors: 0, Skipped: 0");
		fop.flush();
		fop.close();
	}

	
	// 方法级别并发执行
	public void executorMethodCases(Map<Class<?>, List<String>> failcasesMap,
			String logPath, String resultPath, String logType)
			throws ExecutionException, IOException {
		// 失败cases, key:Class, value:List of methods
		Map<Class<?>, List<String>> runcaseMap;
		runcaseMap = failcasesMap;

		int failNum = 0;
		int successNum = 0;
		int casesNum = 0;
		long runTime = 0L;
		List<Result> methodsResult = new ArrayList<Result>();

		// 线程池
		ExecutorService executorService = Executors
				.newFixedThreadPool(THREADCOUNT);
		// 存运行结果
		List<Future<Result>> listFr = new ArrayList<Future<Result>>();
		long startTime = System.currentTimeMillis();
		// 多线程执行用例
		for (Map.Entry<Class<?>, List<String>> entry : runcaseMap.entrySet()) {
			Class<?> testClass = entry.getKey();
			List<String> failMethod = entry.getValue();
			casesNum += failMethod.size();
			for (int i = 0; i < failMethod.size(); i++) {
				Future<Result> fr = executorService.submit(new ThreadRunTest(
						testClass, failMethod.get(i)));
				listFr.add(fr);
			}
		}
		// 记录结果
		for (Future<Result> fr : listFr) {
			try {
				while (!fr.isDone())
					;
				Result result = fr.get();
				if (result.wasSuccessful()) {
					successNum++;
				} else {
					failNum++;
					methodsResult.add(result);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				executorService.shutdown();
			}
		}
		long endTime = System.currentTimeMillis();
		runTime = endTime - startTime;
		// 写结果日志
		writeResult(resultPath, methodsResult, failNum, successNum, casesNum,
				runTime, logPath, logType);

		// 回写日志, 根据logType回写不同格式的运行失败用例回日志文件， 简单工厂模式
		WriteLogFactory wlf = new WriteLogFactory();
		wlf.writeLog(logPath, logType, resultPath);
	}

	// 类级别并发执行
	public void executorClassCases(Map<Class<?>, List<String>> failcasesMap,
			String logPath, String resultPath, String logType)
			throws ExecutionException, IOException {
		// 失败cases, key:Class, value:List of methods
		Map<Class<?>, List<String>> runcaseMap;
		runcaseMap = failcasesMap;

		int failNum = 0;
		int successNum = 0;
		int casesNum = 0;
		long runTime = 0L;
		List<Result> methodsResult = new ArrayList<Result>();

		// 线程池
//		ExecutorService executorService = Executors
//				.newFixedThreadPool(THREADCOUNT);
		ExecutorService executorService = Executors.newCachedThreadPool();
		// 存运行结果
		List<Future<List<Result>>> listFr = new ArrayList<Future<List<Result>>>();
		long startTime = System.currentTimeMillis();
		// 多线程执行用例
		for (Map.Entry<Class<?>, List<String>> entry : runcaseMap.entrySet()) {
			Class<?> testClass = entry.getKey();
			List<String> failMethod = entry.getValue();
			casesNum += failMethod.size();
			Future<List<Result>> fr = executorService
					.submit(new ClassThreadRunTest(testClass, failMethod));
			listFr.add(fr);
		}
		// 记录结果
		for (Future<List<Result>> fr : listFr) {
			try {
				while (!fr.isDone())
					;
				// 单个类的结果
				List<Result> listResult = fr.get();
				for (int i = 0; i < listResult.size(); i++) {
					if (listResult.get(i).wasSuccessful()) {
						successNum++;
					} else {
						failNum++;
						methodsResult.add(listResult.get(i));
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				executorService.shutdown();
			}
		}
		long endTime = System.currentTimeMillis();
		runTime = endTime - startTime;
		// 写结果日志
		writeResult(resultPath, methodsResult, failNum, successNum, casesNum,
				runTime, logPath, logType);

		// 回写日志, 根据logType回写不同格式的运行失败用例回日志文件， 简单工厂模式
		WriteLogFactory wlf = new WriteLogFactory();
		wlf.writeLog(logPath, logType, resultPath);

	}

	// 串行执行
	public void SerialExecutorCases(Map<Class<?>, List<String>> failcasesMap,
			String logPath, String resultPath, String logType)
			throws ExecutionException, IOException {
		// 失败cases, key:Class, value:List of methods
		Map<Class<?>, List<String>> runcaseMap;
		runcaseMap = failcasesMap;

		int failNum = 0;
		int successNum = 0;
		int casesNum = 0;
		long runTime = 0L;

		// 记录失败用例
		List<Result> methodsResult = new ArrayList<Result>();

		// 存运行结果
		List<Result> listFr = new ArrayList<Result>();
		long startTime = System.currentTimeMillis();

		// 遍历执行用例
		for (Map.Entry<Class<?>, List<String>> entry : runcaseMap.entrySet()) {
			Class<?> testClass = entry.getKey();
			List<String> failMethod = entry.getValue();
			casesNum += failMethod.size();
			for (int i = 0; i < failMethod.size(); i++) {
				JUnitCore junitRunner = new JUnitCore();
				Request request = Request.method(testClass, failMethod.get(i));
				Result result = junitRunner.run(request);
				listFr.add(result);
			}
		}

		// 将失败用例添加到methodsResult, 并统计成功和失败数
		for (Result finResult : listFr) {
			if (finResult.wasSuccessful()) {
				++successNum;
			} else {
				++failNum;
				methodsResult.add(finResult);
			}
		}

		long endTime = System.currentTimeMillis();
		runTime = endTime - startTime;
		// 写结果日志
		writeResult(resultPath, methodsResult, failNum, successNum, casesNum,
				runTime, logPath, logType);

		// 回写日志, 根据logType回写不同格式的运行失败用例回日志文件， 简单工厂模式
		WriteLogFactory wlf = new WriteLogFactory();
		wlf.writeLog(logPath, logType, resultPath);

	}
}
